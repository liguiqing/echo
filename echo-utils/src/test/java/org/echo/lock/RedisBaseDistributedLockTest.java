package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : RedisBaseDistributedLock test")
class RedisBaseDistributedLockTest {

    @Test
    void lock() {
        RedissonClient redissonClient = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(redissonClient.getAtomicLong(any(String.class))).thenReturn(rAtomicLong).thenThrow();
        when(rAtomicLong.expireAt(any(Long.class))).thenReturn(Boolean.TRUE);
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(any(String.class))).thenReturn(lock);
        doNothing().when(lock).lock();
        RedisBaseDistributedLock distributedLock = new RedisBaseDistributedLock(redissonClient);
        distributedLock.lock("aa");
        distributedLock.unlock("aa");
        distributedLock.lock("aa");
        distributedLock.unlock("aa");
        assertThrows(Exception.class,()->distributedLock.unlock("bb"));
    }
}