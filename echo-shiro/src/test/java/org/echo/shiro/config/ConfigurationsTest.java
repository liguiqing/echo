package org.echo.shiro.config;

import org.apache.shiro.spring.boot.autoconfigure.ShiroAnnotationProcessorAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroAutoConfiguration;
import org.apache.shiro.spring.boot.autoconfigure.ShiroBeanAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebAutoConfiguration;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.echo.shiro.realm.PrimusSubjectPicker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;

import javax.servlet.Filter;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                ShiroCacheConfiguration.class,
                ShiroEchoConfiguration.class,
                ShiroWebAutoConfiguration.class,
                ShiroEchoWebFilterConfiguration.class,
                ShiroWebFilterConfiguration.class,
                ShiroAutoConfiguration.class,
                ShiroBeanAutoConfiguration.class,
                ShiroAnnotationProcessorAutoConfiguration.class
        })
@SpringBootTest
@DisplayName("Echo : Shiro Configurations Test")
class ConfigurationsTest {
    @Autowired
    private PrimusSubjectPicker primusSubjectPicker;

    @Autowired
    ShiroFilterFactoryBean filterFactoryBean;

    @Test
    void test(){
        assertNotNull(filterFactoryBean);
    }

    @Bean
    Filter formAuthc(){
        FormAuthenticationFilter formAuthenticationFilter = new FormAuthenticationFilter();
        formAuthenticationFilter.setPasswordParam("password");
        formAuthenticationFilter.setUsernameParam("username");
        return formAuthenticationFilter;
    }
}