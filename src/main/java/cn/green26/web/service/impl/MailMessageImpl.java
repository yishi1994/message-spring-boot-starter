package cn.green26.web.service.impl;

import cn.green26.web.config.MailProperties;
import cn.green26.web.config.SMTPProperties;
import cn.green26.web.model.EnumMailContentType;
import cn.green26.web.model.MailMessage;
import cn.green26.web.model.MailReceiver;
import cn.green26.web.service.IMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Optional;
import java.util.Properties;
import java.util.concurrent.*;

@Service
@EnableConfigurationProperties(SMTPProperties.class)
public class MailMessageImpl implements IMessage<MailMessage, MailReceiver> {
    private Message message;

    @Autowired
    @Qualifier("smt")
    private SMTPProperties smtpProperties;

    @Autowired
    private MailProperties mailProperties;

    private final static ExecutorService executorService= Executors.newSingleThreadExecutor();

    private Session getSession(Properties properties) {
        return Session.getInstance(properties, new Authenticator() {
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                String username = smtpProperties.getUsername();
                String password = smtpProperties.getPassword();
                return new javax.mail.PasswordAuthentication(username, password);
            }
        });
    }

    private void wrapMailMessage(Session session) {
        message = new MimeMessage(session);
    }

    private void setReceivers(MailReceiver receiver) throws MessagingException {
        if (receiver.getTo() != null) {
            for (String t : receiver.getTo()) {
                message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(t));
            }
        }

        if (receiver.getCc() != null) {
            for (String c : receiver.getCc()) {
                message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(c));
            }
        }

        if (receiver.getBcc() != null) {
            for (String bc : receiver.getBcc()) {
                message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bc));
            }
        }
    }

    private void setMessage(MailMessage mailMessage) throws MessagingException {
        message.setFrom(new InternetAddress(mailProperties.getFrom()));
        message.setSubject(mailMessage.getSubject());
        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mailMessage.getBody(),
                EnumMailContentType.getValueByCode(mailMessage.getContentType().name()));
        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);
        message.setContent(multipart);
        message.saveChanges();
    }

    public Properties getSMTPProperties() {
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.from", mailProperties.getFrom());
        properties.setProperty("mail.smtp.host", smtpProperties.getHost());
        properties.setProperty("mail.smtp.port", smtpProperties.getPort());
        properties.setProperty("mail.smtp.starttls.enable", smtpProperties.getTlsEnable());
        properties.setProperty("mail.smtp.auth", smtpProperties.getAuth());
        properties.setProperty("mail.smtp.username", smtpProperties.getUsername());
        properties.setProperty("mail.smtp.password", smtpProperties.getPassword());
        return properties;
    }

    class MailHandler implements Callable<Boolean> {
        private final MailMessage mailMessage;
        private final MailReceiver receiver;

        public MailHandler(MailMessage mailMessage, MailReceiver mailReceiver) {
            this.mailMessage = mailMessage;
            this.receiver = mailReceiver;
        }

        @Override
        public Boolean call() throws Exception {
            Session session = getSession(getSMTPProperties());
            wrapMailMessage(session);
            setReceivers(receiver);
            setMessage(mailMessage);
            Transport.send(message);
            return true;
        }
    }

    @Override
    public boolean send(MailMessage mailMessage, MailReceiver receiver)
            throws ExecutionException, InterruptedException, TimeoutException {
        Future<Boolean> result = executorService.submit(new MailHandler(mailMessage, receiver));
        if (result.isDone()) {
            return result.get();
        }
        return false;
    }
}
