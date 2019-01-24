package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : RedisBaseDistributedLock test")
class RedisBaseDistributedLockTest {

    @Test
    void lock() throws Exception{
        Callable c = mock(Callable.class);
        when(c.call()).thenReturn("aa").thenThrow(NullPointerException.class);
        DistributedLock iLock = new DistributedLock(){};
        assertEquals("aa", iLock.lock("bb",c));
        assertNull(iLock.lock("cc", c));


        RedissonClient redissonClient = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(redissonClient.getAtomicLong(any(String.class))).thenReturn(rAtomicLong).thenThrow();
        when(rAtomicLong.expireAt(any(Long.class))).thenReturn(Boolean.TRUE);
        RLock lock = mock(RLock.class);
        when(redissonClient.getFairLock(any(String.class))).thenReturn(lock);
        doNothing().when(lock).lock();
        when(lock.tryLock(any(Long.class), eq(TimeUnit.SECONDS))).thenReturn(false).thenReturn(false).thenReturn(true);
        RedisBaseDistributedLock distributedLock = new RedisBaseDistributedLock(redissonClient);
        distributedLock.lock("aa",()->"");

        distributedLock.lock("aa",()->"");
        when(lock.tryLock(any(Long.class), eq(TimeUnit.SECONDS))).thenReturn(false);
        distributedLock.lock("aa",()->"");

        when(lock.tryLock(any(Long.class),any(TimeUnit.class))).thenThrow(new InterruptedException());
        distributedLock.lock("aa",()->"");

        RedisLockTestBean bean1 = new RedisLockTestBean(1L,"Test");
        distributedLock.lock(bean1,()->"");

        RedisBaseDistributedLock distributedLock1 = new RedisBaseDistributedLock("echo",null);
        distributedLock1.lock("aa",()->"");

        distributedLock1.lock("aa",()->"");

        bean1 = new RedisLockTestBean(1L,"Test");
        distributedLock1.lock(bean1,()->"");
        assertNull(distributedLock1.lock("AA", c));

    }
}