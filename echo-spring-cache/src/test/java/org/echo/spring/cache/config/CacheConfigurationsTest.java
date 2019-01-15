package org.echo.spring.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
            CacheConfigurations.class,
            SecondaryCacheAutoConfiguration.class
}))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml"})
@Slf4j
@DisplayName("Echo : Spring-cache module Configurations exec")
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
    }
}