package cn.green26.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "internal")
@Configuration
@Data
public class InternalProperties {
    /**
     * history message size
     */
    private int history;
}
