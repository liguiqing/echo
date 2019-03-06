package org.echo.spring.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.spring.cache.CacheTestValue;
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
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@Slf4j
@DisplayName("Echo : Spring-cache Configurations Test")
class CacheConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void cacheDequeFactory()throws Exception{
        RedisCacheConfigurations scc = new RedisCacheConfigurations();
        CacheDequeFactory cacheDequeFactory = scc.cacheDequeFactory(null);
        assertNotNull(cacheDequeFactory);
        assertNotNull(cacheDequeFactory.getDeque(""));
        RedissonClient client = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(client.getAtomicLong(any(String.class))).thenReturn(rAtomicLong);
        assertNotNull(scc.cacheDequeFactory(client));

        CacheTestValue v1 = new CacheTestValue().setV1("v1").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheConfigurations cs = new CacheConfigurations();
        Method m = v1.getClass().getMethod("getV1");
        assertEquals("org.echo.spring.cache.CacheTestValuegetV1",cs.keyGenerator().generate(v1,m));
        assertEquals("org.echo.spring.cache.CacheTestValuegetV1ab",cs.keyGenerator().generate(v1,m,"a","b"));
    }
}