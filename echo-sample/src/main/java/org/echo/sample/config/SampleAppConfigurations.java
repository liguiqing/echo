package org.echo.sample.config;

import org.echo.ddd.support.config.DddSupportConfigurations;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import javax.persistence.PersistenceContext;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Configuration
@EnableCaching
@EnableJpaRepositories(value = "org.echo.sample.domain.model.**.*",
        includeFilters = {@ComponentScan.Filter(type= FilterType.ANNOTATION,value= Repository.class)})
@ComponentScan(value = "org.echo.sample.**",
        includeFilters = {
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Service.class),
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Component.class)},
        useDefaultFilters = false)
@Import({DddSupportConfigurations.class})
@PersistenceContext()
public class SampleAppConfigurations {

}