package org.echo.share.config;

import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ContextHierarchy(@ContextConfiguration(classes = {
        CacheConfigurations.class,
        DataSourceConfigurations.class
}))
public class ConfigurationsTest extends AbstractConfigurationsTest {


    public ConfigurationsTest(){

    }
    @Test
    public void test(){

    }
}