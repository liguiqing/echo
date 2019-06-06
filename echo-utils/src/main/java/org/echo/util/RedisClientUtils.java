package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
public class RedisClientUtils {
    private static final String REDIS_ALIVE_TESTING_KEY = "RedisAliveTestingKey";

    private RedisClientUtils(){
        throw new AssertionError("No org.echo.util.RedisClientUtils instances for you!");
    }

    public static boolean isAlive(RedissonClient redissonClient){
        if(redissonClient == null)
            return false;

        //更理想的实现是定时去侦测
        RAtomicLong rAtomicLong = redissonClient.getAtomicLong(REDIS_ALIVE_TESTING_KEY);
        rAtomicLong.expireAt(1L);
        return true;
    }
}