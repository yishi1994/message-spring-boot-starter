package cn.green26.web.service.impl;

import cn.green26.web.model.InternalMessage;
import cn.green26.web.model.InternalMessageReceiver;
import cn.green26.web.service.IMessage;
import cn.green26.web.service.IReceiver;
import org.springframework.stereotype.Service;

@Service
public class InternalMessageImpl implements IMessage<InternalMessage, InternalMessageReceiver> {

    @Override
    public boolean send(InternalMessage message, InternalMessageReceiver receiver) {
        return false;
    }
}
