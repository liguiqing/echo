package org.echo.lock;

import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import org.echo.util.RedisClientUtils;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 基于Redisson实现的分布式锁
 *
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
public class RedisBaseDistributedLock implements DistributedLock<Object> {
    private static final String LOCK_KEY = "lock:";

    private String lockPrefix = "echo:";

    private RedissonClient redissonClient;

    private static final ConcurrentMap<Object, Lock> locks = Maps.newConcurrentMap();

    public RedisBaseDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void lock(Object key){
        locks.putIfAbsent(key,newLock(key));
        locks.get(key).lock();
    }

    @Override
    public void unlock(Object key){
        Lock lock = locks.get(key);
        locks.remove(key);
        Preconditions.checkNotNull(lock);
        lock.unlock();
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