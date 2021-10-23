package cn.green26.web.service.impl;

import cn.green26.web.model.KafkaMessage;
import cn.green26.web.model.KafkaMessageReceiver;
import cn.green26.web.service.IMessage;
import org.springframework.stereotype.Service;

@Service
public class KafkaMessageImpl implements IMessage<KafkaMessage, KafkaMessageReceiver> {

    @Override
    public boolean send(KafkaMessage message, KafkaMessageReceiver receiver) {
        return false;
    }
}
