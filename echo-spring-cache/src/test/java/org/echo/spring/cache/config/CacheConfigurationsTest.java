package org.echo.spring.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
            RedisCacheConfigurations.class,SecondaryCacheConfigurations.class,CacheConfigurations.class
}))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@Slf4j
@DisplayName("Echo : Spring-cache Configurations Test")
class CacheConfigurationsTest extends AbstractConfigurationsTest {

    @Autowired
    KeyGenerator keyGenerator;

    @Autowired
    CacheManager cacheManager;

    @Test
    public void test(){
        assertNotNull(cacheManager);
        Cache cache = cacheManager.getCache("test");
        assertNotNull(cache);
        String s = cache.get("test",()->"Test");
        assertEquals("Test",s);
        cache.clear();
        assertNotNull(keyGenerator);
        try {
            Method method = TestKeyGenerator.class.getMethod("getKey");
            Object o = keyGenerator.generate(new TestKeyGenerator(), method, "");
            assertEquals("org.echo.spring.cache.config.TestKeyGeneratorgetKey",o);
        } catch (NoSuchMethodException e) {
            log.warn(ThrowableToString.toString(e));
        }
        cacheManager.getCache("tesT");
    }

    @Test
    public void cacheDequeFactory(){
        RedisCacheConfigurations scc = new RedisCacheConfigurations();
        CacheDequeFactory cacheDequeFactory = scc.cacheDequeFactory(null);
        assertNotNull(cacheDequeFactory);
        assertNotNull(cacheDequeFactory.getDeque(""));
        RedissonClient client = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(client.getAtomicLong(any(String.class))).thenReturn(rAtomicLong);
        assertNotNull(scc.cacheDequeFactory(client));
    }
}