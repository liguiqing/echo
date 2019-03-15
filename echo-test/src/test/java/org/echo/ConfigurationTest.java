package org.echo;

import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @author Liguiqing
 * @since V1.0
 */

@DisplayName("Echo Test : Configuration")
@EnableConfigurationProperties(TestBean.class)
public class ConfigurationTest extends AbstractConfigurationsTest {
    @Autowired
    Environment env;

    @Autowired
    private TestBean testBean;
    @Test
    public void test(){
        assertNotNull(env);
        assertEquals(5000,testBean.getCaffeine().getExpireAfterAccess());
    }
}