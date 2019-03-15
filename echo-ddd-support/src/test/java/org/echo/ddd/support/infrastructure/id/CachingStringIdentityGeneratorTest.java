package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.domain.id.IdPrefix;
import org.echo.ddd.domain.id.Identity;
import org.echo.ddd.support.domain.model.id.IdLessThenWarned;
import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.echo.lock.DistributedLock;
import org.echo.xcache.CacheDequeFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.Deque;
import java.util.concurrent.ConcurrentLinkedDeque;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CachingStringIdentityGenerator Test")
class CachingStringIdentityGeneratorTest {

    @RepeatedTest(5)
    void test(){

        Class clazz = IdPrefixTestStringIdentity.class;
        IdPrefixBean prefixBean = spy(new IdPrefixBean(clazz.getName(),"IPT",1L));


        IdPrefix<Class<? extends Identity>> idPrefix = mock(IdPrefix.class);
        when(idPrefix.of(eq(clazz))).thenReturn("IPT");


        DistributedLock lock = new DistributedLock(){};//mock(DistributedLock.class);
        IdPrefixBeanRepository repository = mock(IdPrefixBeanRepository.class);
        when(repository.loadOf(any(Integer.class))).thenReturn(null).thenReturn(prefixBean);
        when(repository.save(any(IdPrefixBean.class))).thenReturn(prefixBean);

        Deque<Long> deque = new ConcurrentLinkedDeque<>();
        CacheDequeFactory cacheDequeFactory = mock(CacheDequeFactory.class);
        when(cacheDequeFactory.getDeque(any(String.class))).thenReturn(deque);

        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
        new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);

        CachingStringIdentityGenerator identityGenerator = new CachingStringIdentityGenerator(repository,idPrefix,cacheDequeFactory,lock);
        identityGenerator.setStep(200);
        assertNotNull(identityGenerator.genId());
        identityGenerator.setWithPrefix(false);
        int i = testGenId(identityGenerator,1,"",200);
        identityGenerator.setWithPrefix(true);
        i = testGenId(identityGenerator,i,"IPT",200);
        i = testGenId(identityGenerator,i,"IPT",2000);
        i = testGenId(identityGenerator,i,"IPT",20000);

        IdLessThenWarned warned = new IdLessThenWarned(prefixBean);
        assertNotEquals(warned,new IdLessThenWarned(prefixBean));
    }

    private int testGenId(CachingStringIdentityGenerator identityGenerator,int index,String prefix,int i){
        int size = index+i;
        for(;index<=size;index++){
            String id = identityGenerator.genId(IdPrefixTestStringIdentity.class);
            assertTrue(id.contains(prefix));
        }
        return index;
    }
}