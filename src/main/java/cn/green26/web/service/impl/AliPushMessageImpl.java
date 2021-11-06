package cn.green26.web.service.impl;

import cn.green26.web.model.AlibabaPushMessage;
import cn.green26.web.service.IMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class AliPushMessageImpl implements IMessage<AlibabaPushMessage, String, Boolean> {
    @Autowired
    AlibabaCloudPush cloudPush;

    @Override
    public Boolean send(AlibabaPushMessage alibabaPush, String receiver) {
        try {
            Future<Boolean> result = cloudPush.push(alibabaPush, receiver);
            return result.get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            e.printStackTrace();
        }
        return Boolean.FALSE;
    }
}

