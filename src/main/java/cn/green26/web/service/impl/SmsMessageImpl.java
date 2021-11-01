package cn.green26.web.service.impl;

import cn.green26.web.config.SmsProperties;
import cn.green26.web.model.SmsMessage;
import cn.green26.web.model.SmsReceiver;
import cn.green26.web.service.IMessage;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.utils.StringUtils;
import com.aliyun.teaopenapi.models.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class SmsMessageImpl implements IMessage<SmsMessage, SmsReceiver> {
    @Autowired
    private SmsProperties smsProperties;

    private Client getClient() throws Exception {
        Config config = new Config();
        config.accessKeyId = smsProperties.getAccessKeyId();
        config.accessKeySecret = smsProperties.getAccessKeySecret();
        config.regionId = smsProperties.getRegionId();
        return new Client(config);
    }



    @Override
    public boolean send(SmsMessage message, SmsReceiver receiver) throws Exception {
        SendSmsRequest sendReq = new SendSmsRequest()
                .setPhoneNumbers(StringUtils.join(",", receiver.getMobiles()))
                .setSignName(smsProperties.getSignName())
                .setTemplateCode(smsProperties.getSMSParameters())
                .setTemplateParam("{\"code\":" + generateNumberVerifyCode(this.smsProperties.getLength()) + "}");
        SendSmsResponse sendResp = getClient().sendSms(sendReq);
        String code = sendResp.body.code;
        return code.equals("OK");
    }


    private String generateNumberVerifyCode(int verifySize) {
        return this.generateVerifyCode(verifySize, this.smsProperties.getRange());
    }

    private String generateVerifyCode(int verifySize, String sources) {
        if (sources == null || sources.length() == 0) {
            sources = this.smsProperties.getRange();
        }

        int codesLen = sources.length();
        Random rand = new Random(System.currentTimeMillis());
        StringBuilder verifyCode = new StringBuilder(verifySize);

        for(int i = 0; i < verifySize; ++i) {
            verifyCode.append(sources.charAt(rand.nextInt(codesLen - 1)));
        }

        return verifyCode.toString();
    }
}
