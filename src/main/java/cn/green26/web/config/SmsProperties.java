package cn.green26.web.config;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "sms")
@Configuration
@Getter
@Setter
public class SmsProperties {
    /**
     * SMS max number at one time
     */
    private String max;

    /**
     * SMS key from supplier
     */
    private String key;
}
