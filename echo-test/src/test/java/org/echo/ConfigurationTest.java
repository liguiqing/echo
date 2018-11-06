package org.echo;

import lombok.extern.slf4j.Slf4j;
import org.echo.test.config.JunitTestConfigurations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JunitTestConfigurations.class},
        initializers = ConfigFileApplicationContextInitializer.class)
@Slf4j
@DisplayName("Echo Test : Configuration")
@EnableConfigurationProperties(TestBean.class)
public class ConfigurationTest {
    @Autowired
    Environment env;

    @Autowired
    private TestBean testBean;
    @Test
    public void test(){
        log.debug("I am testing ...");
        assertNotNull(env);

        assertEquals(5000,testBean.getCaffeine().getExpireAfterAccess());

    }

}