package org.echo.share.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ContextHierarchy(@ContextConfiguration(classes = {
        CacheConfigurations.class,
        DataSourceConfigurations.class
}))
@Slf4j
@DisplayName("Echo : Share module Configurations test")
public class ConfigurationsTest extends AbstractConfigurationsTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    public void test(){
        log.debug("I will testing ...");
        assertNotNull(jdbcTemplate);
    }
}