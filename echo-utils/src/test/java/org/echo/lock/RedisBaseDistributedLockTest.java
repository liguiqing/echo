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
        DistributedLock<String,String> iLock = new DistributedLock(){};
        assertEquals("aa", iLock.lock("bb",c));
        iLock.lock("aa","AA",s -> assertEquals("AA",s));
        assertThrows(RuntimeException.class,()->iLock.lock("cc", c));

        RedissonClient redissonClient = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(redissonClient.getAtomicLong(any(String.class))).thenReturn(rAtomicLong).thenThrow();
        when(rAtomicLong.expireAt(any(Long.class))).thenReturn(Boolean.TRUE);
        RLock lock = mock(RLock.class);
        when(redissonClient.getFairLock(any(String.class))).thenReturn(lock);
        doNothing().when(lock).lock();
        when(lock.tryLock(any(Long.class), eq(TimeUnit.SECONDS))).thenReturn(false).thenReturn(false).thenReturn(true);
        RedisBaseDistributedLock<String,String> distributedLock = new RedisBaseDistributedLock(redissonClient);
        distributedLock.lock("aa",()->"");
        distributedLock.lock("aa","aa",s -> assertEquals("aa",s));

        distributedLock.lock("aa",()->"");
        when(lock.tryLock(any(Long.class), eq(TimeUnit.SECONDS))).thenReturn(false);
        distributedLock.lock("aa",()->"");

        when(lock.tryLock(any(Long.class),any(TimeUnit.class))).thenThrow(new InterruptedException());
        distributedLock.lock("aa",()->"");

        RedisBaseDistributedLock<String,RedisLockTestBean> distributedLock2 = new RedisBaseDistributedLock(redissonClient);
        RedisLockTestBean bean1 = new RedisLockTestBean(1L,"Test");
        distributedLock2.lock("aa",bean1,(b)->b.fetch());

        RedisBaseDistributedLock<String,String> distributedLock1 = new RedisBaseDistributedLock("echo",null);
        distributedLock1.lock("aa",()->"");
        distributedLock1.lock("aa",()->"");
        assertNull(distributedLock1.lock("AA", c));

        assertThrows(LockFailureException.class,()->{throw new LockFailureException("aa");});
        assertThrows(LockFailureException.class,()->{throw new LockFailureException();});
    }
}