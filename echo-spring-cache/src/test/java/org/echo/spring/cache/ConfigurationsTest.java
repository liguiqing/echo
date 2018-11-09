package org.echo.spring.cache;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.secondary.SecondaryCacheAutoConfiguration;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

/**
 * @author Liguiqing
 * @since V3.0
 */

@ContextHierarchy(@ContextConfiguration(classes = {
        SecondaryCacheAutoConfiguration.class
}))
@Slf4j
@DisplayName("Echo : Spring cache module Configurations test")
public class ConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void test(){
        log.debug("I will testing ...");
    }
}