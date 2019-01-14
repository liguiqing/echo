package org.echo.test.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@PropertySource(value={"classpath:/application.yml"})
public class JunitTestConfigurations {

}