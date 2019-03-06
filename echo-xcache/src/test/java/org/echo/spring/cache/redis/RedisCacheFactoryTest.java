package org.echo.spring.cache.redis;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.config.RedisCacheConfigurations;
import org.echo.spring.cache.secondary.SecondaryCacheProperties;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@Slf4j
@DisplayName("Echo : Spring-cache RedisCacheFactory Test")
class RedisCacheFactoryTest {

    @Mock
    private RedisCacheProperties redisCacheProperties;

    @Mock
    private RedisTemplate template;

    @Mock
    private JedisPool jedisPool;

    @Mock
    private Jedis jedis;

    @BeforeEach
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void newCache() {
        when(jedisPool.getResource()).thenReturn(jedis);
        when(jedis.isConnected()).thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        when(redisCacheProperties.getName()).thenReturn("Test2");

        Map<String,Long> expires = Maps.newHashMap();
        expires.put("Test", 1000L);
        expires.put("Test1", 1000L);
        expires.put("Test2", 1000L);
        redisCacheProperties.setName("Test2");
        assertEquals("Test2",redisCacheProperties.getName());
        redisCacheProperties.setExpires(expires);
        RedisCacheFactory redisCacheFactory  = new RedisCacheFactory(redisCacheProperties,jedisPool,template);

        Cache cache1 = redisCacheFactory.newCache("Test11",1,1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("Test2");
        assertNotNull(cache1);

        cache1 = redisCacheFactory.newCache("Test3",1,1);
        assertNotNull(cache1);

        Cache cache = redisCacheFactory.newCache("Test");
        assertNotNull(cache);
        assertTrue(cache instanceof RedisNoneCache);
    }
}