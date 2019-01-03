package org.echo.lock;

import lombok.AllArgsConstructor;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

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

    public RedisBaseDistributedLock(RedissonClient redissonClient) {
        this.redissonClient = redissonClient;
    }

    @Override
    public void lock(Object key){
        this.getLock(key.toString()).lock();
    }

    @Override
    public void unlock(Object key){
        this.getLock(key.toString()).unlock();
    }

    private RLock getLock(String key){
        return redissonClient.getLock(getKey(key));
    }

    private String getKey(String key){
        return this.lockPrefix.concat(LOCK_KEY).concat(key);
    }
}