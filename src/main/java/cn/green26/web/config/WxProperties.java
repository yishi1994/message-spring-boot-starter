package cn.green26.web.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "wx")
@Setter
@Getter
public class WxProperties {

    private String appId;

    private String secret;


}
