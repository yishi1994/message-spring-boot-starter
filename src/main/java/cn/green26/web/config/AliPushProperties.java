package cn.green26.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "push")
@Setter
@Getter
public class AliPushProperties {

    private String accessKeyId;

    private String accessKeySecret;

    private String regionId;

    private Long appKeyAndroid;

    private Long appKeyIOS;
}
