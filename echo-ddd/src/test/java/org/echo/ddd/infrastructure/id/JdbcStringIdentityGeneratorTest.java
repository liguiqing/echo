package org.echo.ddd.infrastructure.id;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Sets;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.test.config.AbstractConfigurationsTest;
import org.echo.util.NumbersUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                DataSourceConfigurations.class
        }))
@Transactional
@TestPropertySource(properties = {"spring.config.location = classpath:/application-ddd.yml"})
@Rollback
@DisplayName("Echo : JdbcStringIdentityGenerator Test")
@Slf4j
class JdbcStringIdentityGeneratorTest extends AbstractConfigurationsTest {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @RepeatedTest(5)
    void test()throws Exception {
        assertNotNull(jdbcTemplate);
        jdbcTemplate.update("DROP TABLE IF EXISTS t_cm_dddId;");
        JdbcStringIdentityGenerator jdbcStringIdentityGenerator = new JdbcStringIdentityGenerator(jdbcTemplate,1);
        jdbcStringIdentityGenerator.createIdTable();
        jdbcStringIdentityGenerator.createIdTable();
        assertNotNull(jdbcStringIdentityGenerator.genId());
        jdbcStringIdentityGenerator.clearPrefix("TSA");
        jdbcStringIdentityGenerator.newPrefix("TEST","TSA","echo.test.Test");
        assertEquals("TSA1",jdbcStringIdentityGenerator.genId("TSA"));
        assertEquals("TSA2",jdbcStringIdentityGenerator.genId("TSA"));
        jdbcStringIdentityGenerator.newPrefix("TEST","TSA","echo.test.Test");
        assertEquals("TSA3",jdbcStringIdentityGenerator.genId("TSA"));
        assertEquals("TSA4",jdbcStringIdentityGenerator.genId("TSA"));
        int threads = 100;
        final Collection<String> ids = Sets.newHashSet();
        CountDownLatch cd = new CountDownLatch(threads);
        Runnable[] rs = new Runnable[threads];
        for(int i=0;i<threads;i++){
            rs[i] = ()->{
                try {
                    Thread.sleep(NumbersUtil.randomBetween(1,5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ids.add(jdbcStringIdentityGenerator.genId("TSA"));
                cd.countDown();
            };
        }
        Arrays.stream(rs).forEach(r->new Thread(r).start());
        cd.await();
        assertEquals(threads, ids.size());
    }
}