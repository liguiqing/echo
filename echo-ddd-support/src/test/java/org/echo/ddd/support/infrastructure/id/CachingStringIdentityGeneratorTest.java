package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CachingStringIdentityGenerator Test")
class CachingStringIdentityGeneratorTest {

    @Test
    void test(){
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        Pattern pattern = Pattern.compile("(?=[A-Z])");
        //provider.addIncludeFilter(new AnnotationTypeFilter(Id.class));

        Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("org.echo.*.domain.**");

        Class clazz = IdPrefixTestStringIdentity.class;
        IdPrefixBean prefixBean = spy(new IdPrefixBean(clazz.getName(),"IPT",1L));

        IdPrefixBeanRepository repository = mock(IdPrefixBeanRepository.class);
        IdPrefix<Class<? extends Identity>> idPrefix = mock(IdPrefix.class);
        when(idPrefix.of(eq(clazz))).thenReturn("IPT");

        CacheDequeFactory cacheDequeFactory = mock(CacheDequeFactory.class);
        DistributedLock lock = mock(DistributedLock.class);

        when(repository.loadOf(any(Integer.class))).thenReturn(null).thenReturn(prefixBean);
        when(repository.save(any(IdPrefixBean.class))).thenReturn(prefixBean);

        Deque<Long> deque = spy(new ConcurrentLinkedDeque<>());
        when(cacheDequeFactory.getDeque(any(String.class))).thenReturn(deque);

        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);

        CachingStringIdentityGenerator identityGenerator = new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
//        Deque<Long> deque = mock(Deque.class);
//        IdPrefixBean prefixBean = spy(new IdPrefixBean());
//        EventHandlers.getInstance().post(new IdLessThenWarned(identityGenerator, prefixBean,deque));

        assertNotNull(identityGenerator.genId());
        String id = identityGenerator.genId(IdPrefixTestStringIdentity.class);
    }
}