package cn.green26.web.service.impl;

import cn.green26.web.config.KafkaProperties;
import cn.green26.web.service.IMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Slf4j
@Service
public class KafkaMessageImpl implements IMessage<String, String,Boolean> {

    @Autowired
    private KafkaProperties kafkaProperties;


    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaProperties.getServices());
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.ACKS_CONFIG, kafkaProperties.getAcks());
        configs.put(ProducerConfig.RETRIES_CONFIG, kafkaProperties.getRetries());
        return new DefaultKafkaProducerFactory<>(configs);
    }


    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }


    @Override
    public Boolean send(String message, String topic) {
        ListenableFuture<SendResult<String, String>> future = kafkaTemplate().send(topic, message);
        future.addCallback(result -> log.info("生产者成功发送消息到topic:{},--- message:{}",
                Objects.requireNonNull(result).getRecordMetadata().topic(),
                result.getProducerRecord().value()),
                ex -> log.error("生产者发送消失败，原因：{}", ex.getMessage()));
        return Boolean.TRUE;
    }

}
