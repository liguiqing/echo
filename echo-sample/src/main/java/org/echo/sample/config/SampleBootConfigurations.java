package org.echo.sample.config;

import org.echo.share.config.DataSourceConfigurations;
import org.echo.share.config.SpringMvcConfiguration;
import org.echo.shiro.config.ShiroConfigurations;
import org.echo.spring.cache.config.AutoCacheConfigurations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@Import({DataSourceConfigurations.class, AutoCacheConfigurations.class,
        SampleAppConfigurations.class, ShiroConfigurations.class, SpringMvcConfiguration.class})
@EnableAspectJAutoProxy
@EnableCaching
public class SampleBootConfigurations {


}