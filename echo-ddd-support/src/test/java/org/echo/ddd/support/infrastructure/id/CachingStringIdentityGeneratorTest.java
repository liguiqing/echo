package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.util.NumbersUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;

import java.util.Deque;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CachingStringIdentityGenerator Test")
class CachingStringIdentityGeneratorTest {

    @RepeatedTest(3)
    void test(){
        ClassPathScanningCandidateComponentProvider provider = new ClassPathScanningCandidateComponentProvider(false);
        Pattern pattern = Pattern.compile("(?=[A-Z])");
        //provider.addIncludeFilter(new AnnotationTypeFilter(Id.class));

        Set<BeanDefinition> beanDefinitionSet = provider.findCandidateComponents("org.echo.*.domain.**");

        Class clazz = IdPrefixTestStringIdentity.class;
        IdPrefixBean prefixBean = spy(new IdPrefixBean(clazz.getName(),"IPT",1L));


        IdPrefix<Class<? extends Identity>> idPrefix = mock(IdPrefix.class);
        when(idPrefix.of(eq(clazz))).thenReturn("IPT");


        DistributedLock lock = new DistributedLock(){};//mock(DistributedLock.class);
        IdPrefixBeanRepository repository = mock(IdPrefixBeanRepository.class);
        when(repository.loadOf(any(Integer.class))).thenReturn(null).thenReturn(prefixBean);
        when(repository.save(any(IdPrefixBean.class))).thenReturn(prefixBean);

        Deque<Long> deque = spy(new ConcurrentLinkedDeque<>());
        CacheDequeFactory cacheDequeFactory = mock(CacheDequeFactory.class);
        when(cacheDequeFactory.getDeque(any(String.class))).thenReturn(deque);

        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);

        CachingStringIdentityGenerator identityGenerator = new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
        assertNotNull(identityGenerator.genId());
        identityGenerator.setWithPrefix(false);
        int i = testGenId(identityGenerator,1,"");
        identityGenerator.setWithPrefix(true);
        testGenId(identityGenerator,i,"IPT");
    }

    private int testGenId(CachingStringIdentityGenerator identityGenerator,int index,String prefix){
        int size = index+200;
        for(;index<=size;index++){
            String id = identityGenerator.genId(IdPrefixTestStringIdentity.class);
            try {
                Thread.sleep(NumbersUtil.randomBetween(20,30));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            assertEquals(prefix.concat(index+""),id);
        }
        return index;
    }
}