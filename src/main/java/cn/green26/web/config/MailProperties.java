package cn.green26.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "mail")
@Configuration
@Data
public class MailProperties {
    /**
     * Mail from
     */
    private String from;
    /**
     * Mail from display name
     */
    private String fromName;
}
