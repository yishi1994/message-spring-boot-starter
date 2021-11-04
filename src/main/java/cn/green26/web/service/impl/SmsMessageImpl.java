package cn.green26.web.service.impl;

import cn.green26.web.common.entity.sms.dto.SMSSendRespDTO;
import cn.green26.web.config.SmsProperties;
import cn.green26.web.service.IMessage;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.teaopenapi.models.Config;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Random;
@Slf4j
@Service
public class SmsMessageImpl implements IMessage<String, String, SMSSendRespDTO> {
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
    public SMSSendRespDTO send(String message, String mobile) throws Exception {
        if(message == null){
            message=generateNumberVerifyCode(this.smsProperties.getLength());
        }
        SendSmsRequest sendReq = new SendSmsRequest()
                .setPhoneNumbers(mobile)
                .setSignName(smsProperties.getSignName())
                .setTemplateCode(smsProperties.getSMSParameters())
                .setTemplateParam("{\"code\":" + message + "}");
        SendSmsResponse sendResp = getClient().sendSms(sendReq);
        SMSSendRespDTO respDTO = new SMSSendRespDTO();
        respDTO.setCreateTime(new Date());
        respDTO.setMobile(mobile);
        if (sendResp != null && sendResp.body.code.equals("OK")) {
            respDTO.setCode(message);
            log.info("短信发送成功,mobile={},code={}",mobile,message);
        } else {
            String errorMessage = sendResp != null ? sendResp.body.message : "";
            respDTO.setErrorMessage(errorMessage);
            log.error("短信发送失败,mobile={},errorMessage={}",mobile,errorMessage);
        }
        return respDTO;
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
