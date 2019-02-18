package org.echo.sample.config;

import org.echo.sample.domain.model.exam.ExamRepository;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.spring.cache.config.CacheConfigurations;
import org.echo.spring.cache.config.RedisCacheConfigurations;
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
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisCacheConfigurations.class,
                CacheConfigurations.class,
                DataSourceConfigurations.class,
                SampleAppConfigurations.class
}))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@DisplayName("Echo : Exam module Configurations")
public class ConfigurationsTest extends AbstractConfigurationsTest {


    @Autowired
    private ExamRepository examRepository;

    @Test
    public void test(){
        assertNotNull(examRepository);
    }
}