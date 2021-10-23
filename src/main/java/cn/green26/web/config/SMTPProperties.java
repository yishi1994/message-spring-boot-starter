package cn.green26.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail.smtp")
public class SMTPProperties {
    /**
     * smtp host name or IP
     */
    private String host;
    /**
     * smtp port, default 25
     */
    private String port = "25";

    /**
     * is tls enabled
     */
    private String tlsEnable;

    /**
     * is smtp server auth
     */
    private String auth = "true";

    /**
     * if smtp server is auth, username is required.
     */
    private String username;

    /**
     * if smtp server is auth, password is required.
     */
    private String password;
}
