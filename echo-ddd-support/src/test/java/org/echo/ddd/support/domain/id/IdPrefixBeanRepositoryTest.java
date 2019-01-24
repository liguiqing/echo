package org.echo.ddd.support.domain.id;

import org.echo.ddd.support.config.DddSupportConfigurations;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.share.config.DataSourceConfigurations;
import org.echo.spring.cache.config.CacheConfigurations;
import org.echo.spring.cache.config.SecondaryCacheAutoConfiguration;
import org.echo.test.repository.AbstractRepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                DataSourceConfigurations.class,
                SecondaryCacheAutoConfiguration.class,
                CacheConfigurations.class,
                DddSupportConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml"})
@Transactional
@Rollback
@DisplayName("Echo : IdPrefixBeanRepository Test")
public class IdPrefixBeanRepositoryTest extends AbstractRepositoryTest {

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