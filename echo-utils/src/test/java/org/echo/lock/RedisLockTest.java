package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : Redis lock test")
public class RedisLockTest {


    private String prefix = "echo:test:";

    @Test
    public void test(){
        JedisPool jedisPool = mock(JedisPool.class);
        Jedis jedis = mock(Jedis.class);
        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.set(any(String.class),any(String.class),any(String.class),any(String.class),any(Integer.class))).thenReturn("OK").thenReturn("NO");
        RedisLock lock = new RedisLock();
        lock.lock(jedisPool, "aa", "bb", 1);
        lock.lock(jedisPool, "aa", "bb", 1);
        lock.unlock(jedisPool, "aa", "bb");
    }


//    private  int getRandomNumberInRange(int min, int max) {
//        return NumbersUtil.randomBetween(min, max);
//    }
}