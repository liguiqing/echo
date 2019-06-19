package org.echo.ddd.support.config;

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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Echo : DddSupportConfigurations Test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                AutoCacheConfigurations.class,
                DataSourceAutoConfiguration.class,
                HibernateJpaAutoConfiguration.class,
                RedisAutoConfiguration.class,
                RedissonAutoConfiguration.class,
                DddSupportConfigurations.class
        })
class DddSupportConfigurationsTest {

    @Autowired
    private IdPrefixBeanRepository repository;

    @Test
    void test(){
        assertNotNull(repository);
    }
}