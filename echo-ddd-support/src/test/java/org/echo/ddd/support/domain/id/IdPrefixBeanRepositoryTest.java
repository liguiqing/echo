package org.echo.ddd.support.domain.id;

import org.echo.ddd.support.config.DddSupportConfigurations;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@DisplayName("Echo : IdPrefixBeanRepository Test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                DataSourceAutoConfiguration.class,
                AutoCacheConfigurations.class,
                HibernateJpaAutoConfiguration.class,
                RedisAutoConfiguration.class,
                RedissonAutoConfiguration.class,
                DddSupportConfigurations.class
        })
@Transactional
@Rollback
public class IdPrefixBeanRepositoryTest  {

    @Autowired
    private IdPrefixBeanRepository repository;

    @Test
    void test(){
        assertTrue(true);
        String prefix = "ABC";
        IdPrefixBean idPrefixBean = new IdPrefixBean("foo.bar.Bar",prefix, 1L);
        repository.save(idPrefixBean);
        IdPrefixBean bean = repository.loadOf(idPrefixBean.getIdClassNameHash());
        assertEquals(idPrefixBean,bean);
        for(int i=1000;i>0;i--){
            bean = repository.loadOf(idPrefixBean.getIdClassNameHash());
        }
        assertEquals(idPrefixBean.getIdClassName(),bean.getIdClassName());
        assertEquals("IdPrefixBean(idClassNameHash=522100336, idClassName=foo.bar.Bar, idPrefix=ABC)",bean.toString());

        bean = repository.findByIdPrefix(prefix);
        assertEquals(idPrefixBean,bean);
        assertNull(repository.findByIdPrefix("as"));
    }
}