package org.echo.spring.cache.redis;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("RedisCaffeineCache Test")
public class RedisCaffeineCacheTest {

    @Test
    public void oneTest(){
        assertTrue(true);

        RedisCaffeineCacheProperties cacheProperties =
                new RedisCaffeineCacheProperties(true,true,"test",false);
        com.github.benmanes.caffeine.cache.Cache<String, Object> manualCache = Caffeine.newBuilder()
                .expireAfterWrite(10, TimeUnit.MINUTES)
                .maximumSize(10_000)
                .build();

        com.github.benmanes.caffeine.cache.Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();

        RedisCaffeineCache cache = new RedisCaffeineCache("testCache",null,cacheBuilder.build(),cacheProperties);

        String k1 = "k1";
        String s1 = "s1";
        cache.put(k1,s1);
        String s1_ = cache.get(k1,()->s1);
        assertEquals(s1,s1_);
        s1_ = manualCache.get(k1,k->createExpensiveGraph(s1))+"";
        assertEquals(s1,s1_);
        s1_ = manualCache.get(k1,k->createExpensiveGraph(s1))+"";
        assertEquals(s1,s1_);
        manualCache.invalidate(k1);

        LocalDateTime now = LocalDateTime.now();
        cache.put("tb1",new TestBean("f1",2l,now));
        final TestBean tb1 =  cache.get("tb1",()->null);
        assertAll("person",
                () -> assertEquals("f1", tb1.getF1()),
                () -> assertEquals(0,tb1.getF2().compareTo(2l)),
                ()->assertEquals(now,tb1.getF3())
        );

        cache.evict(k1);
        s1_ = cache.get(k1,()->null);
        assertNull(s1_);
        cache.clear();
        TestBean tb2 =  cache.get("tb1",()->null);
        assertNull(tb2);
    }

    Object createExpensiveGraph(Object value){
        return value;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class TestBean implements Serializable{
        String f1;
        Long f2;
        LocalDateTime f3 ;
    }
}