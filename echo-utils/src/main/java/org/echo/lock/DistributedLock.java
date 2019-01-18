package org.echo.lock;

import java.util.concurrent.Callable;

/**
 * 分布式锁
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface DistributedLock<K,V> {

    default V lock(K key, Callable<V> call)throws Exception{return call.call();}
}