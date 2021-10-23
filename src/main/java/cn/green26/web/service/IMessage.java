package cn.green26.web.service;

import java.util.concurrent.ExecutionException;

public interface IMessage<M, R> {
    boolean send(M message, R receiver) throws ExecutionException, InterruptedException;
}
