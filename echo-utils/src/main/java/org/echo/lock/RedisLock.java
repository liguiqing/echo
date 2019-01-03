package org.echo.lock;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Collections;

/**
 * Redis分成式事务锁
 *
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class RedisLock {

    private static final String LOCK_SUCCESS = "OK";

    private static final String SET_IF_NOT_EXIST = "NX";

    private static final String SET_WITH_EXPIRE_TIME = "PX";

    private static final Long RELEASE_SUCCESS = 1L;

    private static final String LOCK_KEY = "lock:";

    private String lockPrefix = "echo:";

    /**
     * 尝试获取分布式锁
     * @param jedisPool Redis客户端
     * @param key 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean lock(JedisPool jedisPool, String key, String requestId, int expireTime){
        log.debug("Lock redis key {}->{}",requestId,key);

        String lk = LOCK_KEY;
        String lockKey = lk.concat(lockPrefix).concat(key);
        Jedis jedis = jedisPool.getResource();
        String result = jedis.set(lockKey, requestId, SET_IF_NOT_EXIST, SET_WITH_EXPIRE_TIME, expireTime);
        jedis.close();
        Boolean b = LOCK_SUCCESS.equals(result);
        log.debug("Lock redis key {}->{} {}",requestId,key,b?"success":"failure");
        return b;
    }

    /**
     * 释放分布式锁
     * @param jedisPool Redis客户端
     * @param key 待释放的key
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(JedisPool jedisPool,String key,String requestId){
        log.debug("Unlock redis key {}->{}",requestId,key);

        String lk = LOCK_KEY;
        String lockKey = lk.concat(lockPrefix).concat(key);
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Jedis jedis = jedisPool.getResource();
        Object result = jedis.eval(script, Collections.singletonList(lockKey), Collections.singletonList(requestId));
        jedis.close();
        return RELEASE_SUCCESS.equals(result);
    }
}