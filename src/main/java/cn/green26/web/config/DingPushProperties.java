package cn.green26.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ding")
@Setter
@Getter
public class DingPushProperties {

    private String appKey;

    private String appSecret;

    private String agentId;
    
}
