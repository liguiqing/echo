package org.echo.ddd.support.config;

import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                AutoCacheConfigurations.class,
                DataSourceConfigurations.class,
                DddSupportConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@DisplayName("Echo : DddSupportConfigurations Test")
public class DddSupportConfigurationsTest extends AbstractConfigurationsTest {

    @Autowired
    private IdPrefixBeanRepository repository;

    @Test
    public void test(){
        assertNotNull(repository);
    }
}