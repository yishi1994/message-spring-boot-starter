package cn.green26.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class ThreadPoolConfig {

    @Bean
    public ExecutorService getExecutorService() {
        return Executors.newSingleThreadExecutor();
    }
}