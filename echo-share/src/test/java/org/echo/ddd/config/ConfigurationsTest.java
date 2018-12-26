package org.echo.ddd.config;

import lombok.extern.slf4j.Slf4j;
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

}))
@Slf4j
@DisplayName("Echo : Share module Configurations test")
public class ConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void test(){
        log.debug("I will testing ...");
    }
}