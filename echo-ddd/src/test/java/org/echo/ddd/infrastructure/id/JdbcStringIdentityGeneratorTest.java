package org.echo.ddd.infrastructure.id;

import org.echo.share.config.DataSourceConfigurations;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        classes = {
                DataSourceConfigurations.class
        }))
@Transactional
@Rollback
@DisplayName("Echo : JdbcStringIdentityGenerator test")
class JdbcStringIdentityGeneratorTest extends AbstractConfigurationsTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    void test() {
        assertNotNull(jdbcTemplate);
        IdDbVendorAdapter idDbVendorAdapter = new IdDbVendorAdapter(){};
        JdbcStringIdentityGenerator jdbcStringIdentityGenerator = new JdbcStringIdentityGenerator(jdbcTemplate,idDbVendorAdapter);
        jdbcStringIdentityGenerator.createIdTable();
        String testId = jdbcStringIdentityGenerator.genId("TST");
        assertTrue(testId.equals("TST1"));
    }
}