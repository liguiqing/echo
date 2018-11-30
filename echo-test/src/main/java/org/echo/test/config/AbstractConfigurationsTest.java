package org.echo.test.config;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 * @author Liguiqing
 * @since V1.0
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = ConfigFileApplicationContextInitializer.class,
        classes = {
            JunitTestConfigurations.class
})
public abstract class AbstractConfigurationsTest {

}