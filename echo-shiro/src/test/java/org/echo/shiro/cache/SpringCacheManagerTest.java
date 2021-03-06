package org.echo.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("SpringCacheManager Test")
class SpringCacheManagerTest {

    @Test
    void createCache() {
        org.springframework.cache.CacheManager cacheManager = mock(org.springframework.cache.CacheManager.class);
        org.springframework.cache.Cache cache = mock(org.springframework.cache.Cache.class);
        when(cacheManager.getCache(any(String.class))).thenReturn(cache);
        when(cache.get(any(Object.class))).thenReturn(()->"a").thenReturn(()->"a").thenReturn(()->"a").thenReturn(()->"a");
        when(cache.putIfAbsent(any(Object.class),any(Object.class))).thenReturn(()->"a");
        SpringCacheManager springCacheManager = new SpringCacheManager(cacheManager, 2);
        Cache cache1 = springCacheManager.createCache("a");
        assertNotNull(cache1);
        assertEquals("a",cache1.get("a"));
        assertNotNull(cache1.get("a"));
        assertEquals("a",cache1.put("a","a"));
        assertEquals("a",cache1.remove("a"));
        cache1.clear();
        assertEquals(0,cache1.size());
        assertEquals(0,cache1.keys().size());
        assertEquals(0,cache1.values().size());
    }
}