package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class RedisClientUtilsTest {

    @Test
    void isAlive() {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(RedisClientUtils.class));
        assertFalse(RedisClientUtils.isAlive(null));
        RedissonClient client = mock(RedissonClient.class);
        RAtomicLong atomicLong = mock(RAtomicLong.class);
        when(client.getAtomicLong(any(String.class))).thenReturn(atomicLong);
        assertTrue(RedisClientUtils.isAlive(client));
    }
}