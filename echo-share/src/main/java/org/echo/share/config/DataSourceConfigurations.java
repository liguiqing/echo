package org.echo.share.config;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Properties;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
public class DataSourceConfigurations {


    @Bean("dataSource")
    public DataSource dataSource(@Value("${jdbc.jndi.name:testJndiDs}") String jdbcJndiName,
                                 @Value("${jdbc.url}") String url,
                                 @Value("${jdbc.username}") String username,
                                 @Value("${jdbc.password}") String password) throws SQLException {

        final JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
        dsLookup.setResourceRef(true);
        try{
            log.debug("Get DataSource from jndi {}",jdbcJndiName);
            DataSource dataSource = dsLookup.getDataSource("java:comp/env/jdbc/"+jdbcJndiName);
            return dataSource;
        }catch (Exception e){
            log.debug("DataSource not found with jndi {}",jdbcJndiName);
        }

        log.debug("Create DataSource {} {} {}",url,username,password);
        DruidDataSource druidDataSource = new DruidDataSource();
        druidDataSource.setUrl(url);
        druidDataSource.setUsername(username);
        druidDataSource.setPassword(password);
        druidDataSource.setInitialSize(10);
        druidDataSource.setMinIdle(1);
        druidDataSource.setMaxActive(20);
        druidDataSource.setMaxWait(60000);
        druidDataSource.setTimeBetweenEvictionRunsMillis(60000);
        druidDataSource.setMinEvictableIdleTimeMillis(30000);
        druidDataSource.setValidationQuery("SELECT 'X'");
        druidDataSource.setTestWhileIdle(true);
        druidDataSource.setTestOnBorrow(false);
        druidDataSource.setTestOnReturn(false);
        druidDataSource.setPoolPreparedStatements(true);
        druidDataSource.setMaxPoolPreparedStatementPerConnectionSize(100);
        druidDataSource.setFilters("stat");
        return druidDataSource;
    }

    @Bean("jdbcTemplate")
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        return jdbcTemplate;
    }

    @Bean("jpaVendorAdapter")
    public JpaVendorAdapter jpaVendorAdapter() {
        return new HibernateJpaVendorAdapter();
    }

    @Bean("jpaDialect")
    public HibernateJpaDialect jpaDialect(){
        return new HibernateJpaDialect();
    }

    @Bean("entityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            @Value("${jpa.unit.name:null}") String persistenceUnitName,
            DataSource dataSource,
            HibernateJpaDialect jpaDialect,
            JpaVendorAdapter jpaVendorAdapter,
            Properties jpaProperties) {
        LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();

        //配置persistenceUnitName 必须使用JPA标准,在META-INF目录建立配置
        if(!"null".equalsIgnoreCase(persistenceUnitName) && persistenceUnitName.length() > 0) {
            entityManagerFactoryBean.setPersistenceUnitName(persistenceUnitName);
            entityManagerFactoryBean.setPersistenceXmlLocation("classpath:META-INF/persistence.xml");
        }
        entityManagerFactoryBean.setPackagesToScan("org.echo.*.domain.model.**");
        entityManagerFactoryBean.setDataSource(dataSource);
        entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
        entityManagerFactoryBean.setJpaDialect(jpaDialect);
        entityManagerFactoryBean.setJpaProperties(jpaProperties);

        return entityManagerFactoryBean;
    }

    @Bean("transactionManager")
    public PlatformTransactionManager transactionManager(EntityManagerFactory emf){
        JpaTransactionManager transactionManager = new JpaTransactionManager(emf);
        return transactionManager;
    }

    @Bean
    public Properties jpaProperties(@Value("${hibernate.dialect:org.hibernate.dialect.MySQL5InnoDBDialect}") String dialect,
                                    @Value("${hibernate.hbm2ddl.auto:none}") String auto,
                                    @Value("${hibernate.show_sql:false}") String showSql,
                                    @Value("${hibernate.format_sql:true}") String formatSql,
                                    @Value("${hibernate.jdbc.batch_size:100}") String batchSize,
                                    @Value("${hibernate.jdbc.fetch_size:50}") String fetchSize,
                                    @Value("${hibernate.max_fetch_depth:10}") String fetchDepth){
        Properties jpaProperties  = new Properties();
        jpaProperties.setProperty("hibernate.dialect",dialect);
        jpaProperties.setProperty("hibernate.hbm2ddl.auto",auto);
        jpaProperties.setProperty("hibernate.show_sql",showSql);
        jpaProperties.setProperty("hibernate.format_sql",formatSql);
        jpaProperties.setProperty("hibernate.jdbc.batch_size",batchSize);
        jpaProperties.setProperty("hibernate.jdbc.fetch_size",fetchSize);
        jpaProperties.setProperty("hibernate.max_fetch_depth",fetchDepth);
        //jpaProperties.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.EhCacheRegionFactory");
        return jpaProperties;
    }
}