package cn.green26.web.service.impl;

import cn.green26.web.model.SmsMessage;
import cn.green26.web.model.SmsReceiver;
import cn.green26.web.service.IMessage;
import org.springframework.stereotype.Service;

@Service
public class SmsMessageImpl implements IMessage<SmsMessage, SmsReceiver> {
    @Override
    public boolean send(SmsMessage message, SmsReceiver receiver) {
        return false;
    }
}
