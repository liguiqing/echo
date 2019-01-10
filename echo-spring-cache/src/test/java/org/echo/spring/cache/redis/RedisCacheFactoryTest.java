package org.echo.spring.cache.redis;

import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class RedisCacheFactoryTest {

    @Test
    void newCache() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
        RedisCacheProperties redisCacheProperties = new RedisCacheProperties();
        Map<String,Long> expires = mock(Map.class);
        Map.Entry<String,Long> entry =mock(Map.Entry.class);
        Set<Map.Entry<String,Long>> set = mock(Set.class);
        Iterator<Map.Entry<String, Long>> iterator = mock(Iterator.class);
        when(iterator.hasNext()).thenReturn(true);
        when(iterator.next()).thenReturn(entry);
        when(set.iterator()).thenReturn(iterator);
        when(expires.get(any(String.class))).thenReturn(1L);
        when(expires.entrySet()).thenReturn(set);
        redisCacheProperties.setExpires(expires);
        RedisCacheFactory redisCacheFactory  = new RedisCacheFactory(connectionFactory,redisCacheProperties);
        Cache cache1 = redisCacheFactory.newCache("test",1,1);
        assertNotNull(cache1);
    }
}