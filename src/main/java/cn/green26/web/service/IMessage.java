package cn.green26.web.service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

public interface IMessage<M, R> {
    boolean send(M message, R receiver) throws Exception;
}
