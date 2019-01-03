package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : RedisBaseDistributedLock test")
class RedisBaseDistributedLockTest {

    @Test
    void lock() {
        RedissonClient redissonClient = mock(RedissonClient.class);
        RLock lock = mock(RLock.class);
        when(redissonClient.getLock(any(String.class))).thenReturn(lock);
        doNothing().when(lock).lock();
        RedisBaseDistributedLock distributedLock = new RedisBaseDistributedLock(redissonClient);
        distributedLock.lock("aa");
        distributedLock.unlock("aa");
    }
}