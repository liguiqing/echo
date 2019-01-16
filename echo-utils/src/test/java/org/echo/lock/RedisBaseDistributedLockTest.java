package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.assertThrows;
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
        when(redissonClient.getFairLock(any(String.class))).thenReturn(lock);
        doNothing().when(lock).lock();
        RedisBaseDistributedLock distributedLock = new RedisBaseDistributedLock(redissonClient);
        distributedLock.lock("aa");
        distributedLock.unlock("aa");
        distributedLock.lock("aa");
        distributedLock.unlock("aa");
        RedisLockTestBean bean1 = new RedisLockTestBean(1L,"Test");
        distributedLock.lock(bean1);
        distributedLock.unlock(bean1);
        assertThrows(Exception.class,()->distributedLock.unlock("bb"));

        RedisBaseDistributedLock distributedLock1 = new RedisBaseDistributedLock("echo",null);
        distributedLock1.lock("aa");
        distributedLock1.unlock("aa");
        distributedLock1.lock("aa");
        distributedLock1.unlock("aa");
        bean1 = new RedisLockTestBean(1L,"Test");
        distributedLock1.lock(bean1);
        distributedLock1.unlock(bean1);
        assertThrows(Exception.class,()->distributedLock1.unlock("bb"));

        DistributedLock dLock = new DistributedLock(){};
        dLock.lock("aa");
        dLock.unlock("aa");
    }
}