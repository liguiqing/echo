package org.echo.sample.config;

import org.echo.share.config.CacheConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.spring.cache.secondary.SecondaryCacheAutoConfiguration;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                SecondaryCacheAutoConfiguration.class,
                CacheConfigurations.class,
                DataSourceConfigurations.class,
                AppConfigurations.class
}))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml"})
@DisplayName("Echo : Exam module Configurations test")
public class ConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void test(){}
}