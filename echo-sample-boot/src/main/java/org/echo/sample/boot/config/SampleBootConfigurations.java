package org.echo.sample.boot.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@EnableAspectJAutoProxy
@EnableCaching
@ComponentScan(basePackages = {"org.echo.sample.**.config"})
@ComponentScan(basePackages = {"org.echo.sample.**.controller"})
@PropertySource("classpath:/META-INF/spring/bootConfig.yml")
public class SampleBootConfigurations {

}