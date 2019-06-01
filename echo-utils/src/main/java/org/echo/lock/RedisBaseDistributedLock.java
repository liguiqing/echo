package org.echo.lock;

import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.util.RedisClientUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * 基于Redisson实现的分布式锁
 *
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
public class RedisBaseDistributedLock<K,V> implements DistributedLock<K,V> {
    private static final String LOCK_KEY = "lock:";

    private String lockPrefix = "echo:";

    private RedissonClient redissonClient;

    private static final ConcurrentMap<Object, Lock> locks = Maps.newConcurrentMap();

    public RedisBaseDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public V lock(K key, Callable<V> call){
        Lock lock = getLock(key);
        try{
            lock.lock();
            return call.call();
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void lock(K key, V o, Consumer<V> consumer) {
        Lock lock = getLock(key);
        lock.lock();
        consumer.accept(o);
        lock.unlock();
    }

    private Lock getLock(K key){
        locks.putIfAbsent(key,newLock(key));
        return locks.get(key);
    }

    private Lock newLock(Object key) {
        if (RedisClientUtils.isAlive(this.redissonClient)) {
            return getRLock(key.toString());
        }
        return new ReentrantLock();
    }

    private RLock getRLock(String key){
        return redissonClient.getFairLock(getKey(key));
    }

    private String getKey(String key){
        return this.lockPrefix.concat(LOCK_KEY).concat(key);
    }
}