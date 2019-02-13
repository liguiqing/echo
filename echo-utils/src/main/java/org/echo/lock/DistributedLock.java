package org.echo.lock;

import org.echo.exception.ThrowableToString;

import java.util.concurrent.Callable;

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
            throw new RuntimeException(e);
        }
    }
}