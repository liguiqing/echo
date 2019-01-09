package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
public class RedisClientUtils {
    private static final String RedisAliveTestingKey = "RedisAliveTestingKey";

    public static boolean isAlive(RedissonClient redissonClient){
        try{
            //更理想的实现是定时去侦测
            RAtomicLong rAtomicLong = redissonClient.getAtomicLong(RedisAliveTestingKey);
            rAtomicLong.expireAt(1L);
            return true;
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }
        return false;
    }
}