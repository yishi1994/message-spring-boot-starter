package cn.green26.web.service;

import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import java.util.Objects;

@Service
public interface IMessage<M, R, T> {
    T send(M message, R receiver) throws ExecutionException, InterruptedException, TimeoutException, Exception;

    /**
     * Send a message to a receiver and try to route it to a specific partition.
     * <p>
     * Default implementation keeps backward compatibility by delegating to the basic
     * {@link #send(Object, Object)} implementation when no partition specific
     * support is provided.
     */
    default T send(M message, R receiver, Integer partition)
            throws ExecutionException, InterruptedException, TimeoutException, Exception {
        return send(message, receiver);
    }

    default T sendBatch(Iterable<M> messages, R receiver)
            throws ExecutionException, InterruptedException, TimeoutException, Exception {
        Objects.requireNonNull(messages, "messages must not be null");
        T lastResult = null;
        for (M message : messages) {
            lastResult = send(message, receiver);
        }
        return lastResult;
    }

    default T sendBatch(Iterable<M> messages, R receiver, Integer partition)
            throws ExecutionException, InterruptedException, TimeoutException, Exception {
        Objects.requireNonNull(messages, "messages must not be null");
        T lastResult = null;
        for (M message : messages) {
            lastResult = send(message, receiver, partition);
        }
        return lastResult;
    }
}
