package org.echo.spring.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.secondary.SecondaryCacheManager;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

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
class AutoConfigurationsTest  extends AbstractConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private SecondaryCacheManager secondaryCacheManager;

    @Test
    public void test(){
        assertTrue(cacheManager instanceof CompositeCacheManager);
        assertTrue(secondaryCacheManager.hasTwoLevel());
    }
}