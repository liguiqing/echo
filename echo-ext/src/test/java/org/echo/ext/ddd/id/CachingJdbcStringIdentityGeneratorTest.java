package org.echo.ext.ddd.id;

import org.assertj.core.util.Sets;
import org.echo.lock.DistributedLock;
import org.echo.lock.RedisBaseDistributedLock;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.test.config.AbstractConfigurationsTest;
import org.echo.util.NumbersUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
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
@Rollback
@DisplayName("Echo : JdbcStringIdentityGenerator exec")
class CachingJdbcStringIdentityGeneratorTest  extends AbstractConfigurationsTest {

    @Autowired
    JdbcTemplate jdbcTemplate;


    @Test
    void genId() throws Exception {
        DistributedLock lock = new RedisBaseDistributedLock(null);
        CacheDequeFactory cacheDequeFactory = new CacheDequeFactory(){};
        CachingJdbcStringIdentityGenerator generator = new CachingJdbcStringIdentityGenerator(jdbcTemplate,cacheDequeFactory,lock);
        generator.setStep(100);
        generator.newPrefix("TEST","TSA","echo.test.Test");
        String id = generator.genId("TSA");
        assertNotNull(id);

        int threads = 100;
        final Collection<String> ids = Sets.newHashSet();
        CountDownLatch cd = new CountDownLatch(threads);
        Runnable[] rs = new Runnable[threads];
        for(int i=0;i<threads;i++){
            rs[i] = ()->{
                try {
                    Thread.sleep(NumbersUtil.randomBetween(5,10));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ids.add(generator.genId("TSA"));
                cd.countDown();
            };
        }
        Arrays.stream(rs).forEach(r->new Thread(r).start());
        cd.await();
        assertEquals(threads, ids.size());

        generator.newPrefix("CMMN","CMMN","");
        generator.setWithPrefix(false);
        assertNotNull(generator.genId(""));
        generator.clearPrefix("CMMN");
    }
}