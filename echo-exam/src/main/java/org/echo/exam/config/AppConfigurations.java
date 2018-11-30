package org.echo.exam.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@EnableCaching
@EnableJpaRepositories(value = "org.echo.exam.domain.model.**.*",
        includeFilters = {@ComponentScan.Filter(type= FilterType.ANNOTATION,value= Repository.class)})
@ComponentScan(value = "org.echo.exam.**",
        includeFilters = {
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Service.class),
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Component.class)},
        useDefaultFilters = false)
@PropertySource("classpath:/META-INF/app.properties")
@PersistenceContext()
public class AppConfigurations {

}