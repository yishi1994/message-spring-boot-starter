package cn.green26.web.config;

import cn.green26.web.model.*;
import cn.green26.web.service.impl.InternalMessageImpl;
import cn.green26.web.service.impl.KafkaMessageImpl;
import cn.green26.web.service.impl.MailMessageImpl;
import cn.green26.web.service.impl.SmsMessageImpl;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cn.green26.web.service.IMessage;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@EnableConfigurationProperties(value = {
        SmsProperties.class,
        KafkaProperties.class,
        InternalProperties.class,
        MailProperties.class,
        SMTPProperties.class
})
@Configuration
public class MessageAutoConfiguration {
    @Autowired
    private KafkaProperties kafkaProperties;
    @Bean
    public IMessage<SmsMessage, SmsReceiver> smsMessage() {
        return new SmsMessageImpl();
    }

    @Bean
    public IMessage<String, String> kafkaMessage() {
        return new KafkaMessageImpl();
    }

    @Bean
    public IMessage<MailMessage, MailReceiver> mailMessage() {
        return new MailMessageImpl();
    }

    @Bean
    public IMessage<InternalMessage, InternalMessageReceiver> internalMessage() {
        return new InternalMessageImpl();
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        if(kafkaProperties == null){
            return null;
        }
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServices());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, kafkaProperties.getAutoOffsetReset());
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, kafkaProperties.getEnableAutoCommit());
        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean("KafkaListenerFactory")
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        if(kafkaProperties == null){
            return null;
        }
        Integer concurrency = kafkaProperties.getConcurrency();
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE);
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }
}
