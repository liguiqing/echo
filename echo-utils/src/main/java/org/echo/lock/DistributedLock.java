package org.echo.lock;

import org.echo.exception.ThrowableToString;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * 分布式锁
 *
 * @author Liguiqing
 * @since V1.0
 */
public interface DistributedLock<K,V> {

    default V lock(K key, Callable<V> call){
        try {
            return call.call();
        } catch (Exception e) {
            ThrowableToString.logWarn(e);
            throw new LockFailureException(e.getMessage());
        }
    }

    default void lock(K key,V v, Consumer<V> consumer){
        consumer.accept(v);
    }
}