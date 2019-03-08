package org.echo.sample.config;

import org.echo.share.config.DataSourceConfigurations;
import org.echo.share.config.SpringMvcConfiguration;
import org.echo.shiro.config.ShiroConfigurations;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@Import({DataSourceConfigurations.class, AutoCacheConfigurations.class,
        SampleAppConfigurations.class, ShiroConfigurations.class, SpringMvcConfiguration.class})
@ComponentScan(value = "org.echo.sample.**.controller",
        includeFilters = {
                @ComponentScan.Filter(type= FilterType.ANNOTATION,value= Controller.class)},
        useDefaultFilters = false)
@ComponentScan(value = "org.echo.sample.**",
        includeFilters = {
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Service.class),
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Component.class)},
        useDefaultFilters = false)
@EnableAspectJAutoProxy
@EnableCaching
public class SampleBootConfigurations {


}