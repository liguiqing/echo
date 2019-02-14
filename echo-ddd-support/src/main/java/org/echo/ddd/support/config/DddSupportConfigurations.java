package org.echo.ddd.support.config;

import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.ddd.support.infrastructure.id.CachingStringIdentityGenerator;
import org.echo.ddd.support.infrastructure.id.IdPrefixGenerator;
import org.echo.ddd.support.infrastructure.id.WordsToString;
import org.echo.ddd.support.infrastructure.id.WordsToUpperCaseString;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
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
@EnableJpaRepositories(value = "org.echo.ddd.support.domain.**.*",
        includeFilters = {@ComponentScan.Filter(type= FilterType.ANNOTATION,value= Repository.class)})
@ComponentScan(value = "org.echo.ddd.support.**",
        includeFilters = {
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Service.class),
                @ComponentScan.Filter(type=FilterType.ANNOTATION,value= Component.class)},
        useDefaultFilters = false)
@PersistenceContext()
public class DddSupportConfigurations {

    @Bean
    public WordsToUpperCaseString wordsToString(){
        return new WordsToUpperCaseString();
    }

    @Bean
    public IdPrefixGenerator idPrefix(@Value("${app.ddd.domain.id.prefix.length:3}")int length, WordsToString wordsToString, IdPrefixBeanRepository repository){
        return new IdPrefixGenerator(length,wordsToString, repository);
    }

    @Bean
    public CachingStringIdentityGenerator cachingStringIdentityGenerator(CacheDequeFactory cacheDequeFactory,
                                                                         IdPrefix<Class<? extends Identity>> idPrefix,
                                                                         DistributedLock distributedLock,
                                                                         IdPrefixBeanRepository repository){
        return new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,distributedLock);
    }

}