package org.echo.spring.cache.caffeine;

import org.assertj.core.util.Lists;
import org.echo.spring.cache.NativeCaches;
import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("CaffeineCacheFactory Test")
class CaffeineCacheFactoryTest {
    @Test
    void test(){

        CaffeineCacheProperties ccp = new CaffeineCacheProperties();
        CaffeineProperties cp = new CaffeineProperties().setName("Test");
        ccp.setDefaultProp(cp);
        assertEquals("Test",cp.getName());
        assertEquals("Test",ccp.getName());
        CaffeineCacheFactory factory = new CaffeineCacheFactory(ccp);
        CaffeineCache cache = (CaffeineCache)factory.newCache("Test");
        assertEquals("Test",cache.getName());
        assertEquals(cp,ccp.getProp("Test"));

        ArrayList<CaffeineProperties> boot = Lists.newArrayList();
        boot.add(new CaffeineProperties().setName("Test1"));
        boot.add(new CaffeineProperties().setName("Test2").setExpireAfterWrite(2000));
        ccp.setCachesOnBoot(boot);
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        ccp.setCachesOnBoot(Lists.newArrayList());
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        boot = Lists.newArrayList();
        boot.add(new CaffeineProperties().setName("Test2").setExpireAfterWrite(2000));
        ccp.setCachesOnBoot(boot);
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        assertThrows(Exception.class,()->
                new PrivateConstructors().exec(CaffeineCaches.class)
        );
    }
}