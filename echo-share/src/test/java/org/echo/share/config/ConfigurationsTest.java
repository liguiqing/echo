package org.echo.share.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.naming.NamingException;
import javax.sql.DataSource;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ContextHierarchy(@ContextConfiguration(classes = {
        TestTemplateLoader.class,
        DataSourceConfigurations.class,
        SpringMvcConfiguration.class
}))
@Slf4j
@DisplayName("Echo : Share module Configurations ")
@WebAppConfiguration
@EnableWebMvc
public class ConfigurationsTest extends AbstractConfigurationsTest {
    static SimpleNamingContextBuilder builder;

    @BeforeAll
    public static void beforeClass(){
        try {
            builder = new SimpleNamingContextBuilder();
            builder.bind("java:comp/env/jdbc/testJndiDs", new Object());
            builder.activate();
        } catch (NamingException ex) {
            log.error(ex.getLocalizedMessage());
        }
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    DataSource dataSource;

    @Test
    public void test(){
        log.debug("I will testing ...");
        assertNotNull(jdbcTemplate);
        assertNotNull(dataSource);
    }

    @Test
    public void dataSource()throws Exception{
        DataSourceProperties dp = spy(new DataSourceProperties());

        try {
            builder.clear();
            builder.bind("java:comp/env/jdbc/"+dp.getJndiName(), new DruidDataSource());
            builder.activate();
        } catch (NamingException ex) {
            log.warn(ex.getLocalizedMessage());
        }

        DataSourceConfigurations dc = new DataSourceConfigurations();
        assertNotNull(dc.dataSource(dp));
    }

    @Test
    void entityManagerFactory(){
        DataSourceConfigurations dc = new DataSourceConfigurations();
        DataSource dataSource = mock(DataSource.class);
        HibernateJpaDialect jpaDialect = new HibernateJpaDialect();
        JpaVendorAdapter jpaVendorAdapter = mock(JpaVendorAdapter.class);
        Properties jpaProperties = new Properties();
        assertNotNull(dc.entityManagerFactory("", "", dataSource, jpaDialect, jpaVendorAdapter, jpaProperties));
        assertNotNull(dc.entityManagerFactory("hello", "abc", dataSource, jpaDialect, jpaVendorAdapter, jpaProperties));
        assertNotNull(dc.entityManagerFactory("hello", "classpath:META-INF/persistence.xml", dataSource, jpaDialect, jpaVendorAdapter, jpaProperties));
        assertNotNull(dc.entityManagerFactory("hello", "classpath:META-INF/persistence1.xml", dataSource, jpaDialect, jpaVendorAdapter, jpaProperties));
    }
}