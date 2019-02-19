package org.echo.sample.config;

import org.echo.ddd.support.config.DddSupportConfigurations;
import org.echo.share.config.FreemarkerStaticModels;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

import javax.persistence.PersistenceContext;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Configuration
@EnableCaching
@EnableJpaRepositories(value = "org.echo.sample.domain.model.**.*",
        includeFilters = {@ComponentScan.Filter(type= FilterType.ANNOTATION,value= Repository.class)})
@Import({DddSupportConfigurations.class})
@PersistenceContext()
public class SampleAppConfigurations {

    @Bean
    public FreemarkerStaticModels freemarkerStaticModels(){
        Map<String,String> maps = FreemarkerStaticModels.defaultStaticModels();
        return new FreemarkerStaticModels(maps);
    }
}