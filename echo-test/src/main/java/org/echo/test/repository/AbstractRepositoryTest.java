package org.echo.test.repository;

import org.echo.test.config.JunitTestConfigurations;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {
        JunitTestConfigurations.class
})
public abstract class AbstractRepositoryTest {


}