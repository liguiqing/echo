package org.echo.shiro.config;

import org.echo.shiro.realm.PrimusSubjectPicker;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                ShiroConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-shiro.yml"})
@DisplayName("Echo : Shiro Configurations Test")
class ConfigurationsTest extends AbstractConfigurationsTest {
    @Autowired
    private PrimusSubjectPicker primusSubjectPicker;
    @Test
    void test(){
        assertNotNull(primusSubjectPicker);
    }
}