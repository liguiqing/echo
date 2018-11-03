package org.echo.test.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JunitTestConfigurations.class
})
@ConfigurationProperties(prefix = "application")
public abstract class AbstractConfigurationsTest {

}