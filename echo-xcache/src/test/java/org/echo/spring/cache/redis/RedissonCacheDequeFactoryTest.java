package org.echo.spring.cache.redis;

import org.junit.jupiter.api.Test;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class RedissonCacheDequeFactoryTest {

    @Test
    void getDeque() {
        RedissonClient client = mock(RedissonClient.class);
        RBlockingDeque deque = mock(RBlockingDeque.class);
        RedisCacheProperties properties = new RedisCacheProperties();
        RedissonCacheDequeFactory factory = new RedissonCacheDequeFactory(client, properties);

        when(client.getBlockingDeque(any(String.class), any(Codec.class))).thenReturn(deque);
        assertNotNull(factory.getDeque("TEst"));
    }
}