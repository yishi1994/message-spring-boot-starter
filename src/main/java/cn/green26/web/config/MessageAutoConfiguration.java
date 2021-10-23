package cn.green26.web.config;

import cn.green26.web.model.*;
import cn.green26.web.service.impl.InternalMessageImpl;
import cn.green26.web.service.impl.KafkaMessageImpl;
import cn.green26.web.service.impl.MailMessageImpl;
import cn.green26.web.service.impl.SmsMessageImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.green26.web.service.IMessage;

@EnableConfigurationProperties(value = {
        SmsProperties.class,
        KafkaProperties.class,
        InternalProperties.class,
        MailProperties.class,
        SMTPProperties.class
})
@Configuration
public class MessageAutoConfiguration {
    @Bean
    public IMessage<SmsMessage, SmsReceiver> smsMessage() {
        return new SmsMessageImpl();
    }

    @Bean
    public IMessage<KafkaMessage, KafkaMessageReceiver> kafkaMessage() {
        return new KafkaMessageImpl();
    }

    @Bean
    public IMessage<MailMessage, MailReceiver> mailMessage() {
        return new MailMessageImpl();
    }

    @Bean
    public IMessage<InternalMessage, InternalMessageReceiver> internalMessage() {
        return new InternalMessageImpl();
    }
}
