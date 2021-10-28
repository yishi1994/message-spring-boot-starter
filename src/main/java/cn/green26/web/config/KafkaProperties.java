package cn.green26.web.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "kafka")
@Configuration
@Data
public class KafkaProperties {
    private String services;
    private String acks;
    private String autoOffsetReset;
    private Integer concurrency;
    private Boolean enableAutoCommit;
    private Integer retries;
}
