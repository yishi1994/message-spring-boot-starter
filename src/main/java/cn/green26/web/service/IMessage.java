package cn.green26.web.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

@Service
public interface IMessage<M, R, T> {
    T send(M message, R receiver) throws ExecutionException, InterruptedException, TimeoutException, Exception;
}
