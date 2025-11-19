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
import org.springframework.util.concurrent.ListenableFuture;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Slf4j
public class KafkaMessageImpl implements IMessage<String, String, Boolean> {

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
        return send(message, topic, null);
    }

    @Override
    public Boolean send(String message, String topic, Integer partition) {
        ListenableFuture<SendResult<String, String>> future;
        if (partition == null) {
            future = kafkaTemplate().send(topic, message);
        } else {
            future = kafkaTemplate().send(topic, partition, null, message);
        }
        addCallback(future);
        return Boolean.TRUE;
    }

    @Override
    public Boolean sendBatch(Iterable<String> messages, String topic) {
        return sendBatch(messages, topic, null);
    }

    @Override
    public Boolean sendBatch(Iterable<String> messages, String topic, Integer partition) {
        if (messages == null) {
            log.warn("批量发送消息为空，topic:{}", topic);
            return Boolean.FALSE;
        }
        Iterator<String> iterator = messages.iterator();
        if (!iterator.hasNext()) {
            log.warn("批量发送消息为空，topic:{}", topic);
            return Boolean.FALSE;
        }
        do {
            send(iterator.next(), topic, partition);
        } while (iterator.hasNext());
        return Boolean.TRUE;
    }

    private void addCallback(ListenableFuture<SendResult<String, String>> future) {
        future.addCallback(result -> {
                    if (result == null || result.getRecordMetadata() == null) {
                        log.warn("生产者发送消息成功，但返回结果为空");
                        return;
                    }
                    log.info("生产者成功发送消息到topic:{}, partition:{}, --- message:{}",
                            result.getRecordMetadata().topic(),
                            result.getRecordMetadata().partition(),
                            result.getProducerRecord().value());
                },
                ex -> log.error("生产者发送消失败，原因：{}", ex.getMessage(), ex));
    }
}
