package cn.green26.web.config;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Data
@Configuration
@ConfigurationProperties(prefix = "sms")
public class SmsProperties {
    /**
     * SMS max number at one time
     */
    private String max;

    /**
     * SMS key from supplier
     */
    private String key;


    private String product;
    private String domain;
    private String accessKeyId;
    private String accessKeySecret;
    private String regionId;
    private String endpointName;
    private int maxTime;
    private String range;
    private int length;
    private String signName;
    private String SMSParameters;

}
