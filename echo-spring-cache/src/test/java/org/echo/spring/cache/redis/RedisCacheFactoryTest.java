package org.echo.spring.cache.redis;

import com.google.common.collect.Maps;
import org.junit.jupiter.api.Test;
import org.redisson.api.RedissonClient;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
class RedisCacheFactoryTest {

    @Test
    void newCache() {
        RedisConnectionFactory connectionFactory = mock(RedisConnectionFactory.class);
        RedisCacheProperties redisCacheProperties = new RedisCacheProperties();
        Map<String,Long> expires = Maps.newHashMap();
        expires.put("Test", 1000L);
        expires.put("Test1", 1000L);
        expires.put("Test2", 1000L);
        redisCacheProperties.setName("Test2");
        assertEquals("Test2",redisCacheProperties.getName());
        redisCacheProperties.setExpires(expires);
        RedisCacheFactory redisCacheFactory  = new RedisCacheFactory(connectionFactory,redisCacheProperties);
        Cache cache1 = redisCacheFactory.newCache("exec",1,1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("exec");
        assertNotNull(cache1);

        redisCacheProperties.setCacheNullValues(false);
        redisCacheFactory  = new RedisCacheFactory(connectionFactory,redisCacheProperties);
        cache1 = redisCacheFactory.newCache("exec",1,1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("exec");
        assertNotNull(cache1);

        RedissonClient redissonClient = mock(RedissonClient.class);
        redisCacheFactory  = new RedisCacheFactory(redissonClient,redisCacheProperties);
        cache1 = redisCacheFactory.newCache("exec",1,1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("exec");
        assertNotNull(cache1);

        cache1 = redisCacheFactory.newCache("exec",-1,-1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("exec");
        assertNotNull(cache1);
    }
}