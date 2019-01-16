package org.echo.lock;

/**
 * 分布式锁
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface DistributedLock<K> {

    default void lock(K key){}

    default void unlock(K key){}
}