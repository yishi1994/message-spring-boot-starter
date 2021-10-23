package cn.green26.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kafka")
@Configuration
@Data
public class KafkaProperties {
    /**
     * kafka topic name
     */
    private String topic;
}
