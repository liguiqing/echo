package org.echo.exam.config;

import org.echo.share.config.CacheConfigurations;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(classes = {
        CacheConfigurations.class,
        DataSourceConfigurations.class,
        AppConfigurations.class
}))
@DisplayName("Echo : Exam module Configurations test")
public class ConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void test(){}
}