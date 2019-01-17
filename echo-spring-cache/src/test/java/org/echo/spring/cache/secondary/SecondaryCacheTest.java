package org.echo.spring.cache.secondary;

import org.echo.spring.cache.NativeCaches;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("SecondaryCache Test")
class SecondaryCacheTest {

    @Test
    void test(){
        Cache l1 = mock(Cache.class);
        Cache l2 = mock(Cache.class);
        Cache.ValueWrapper value  = mock(Cache.ValueWrapper.class);
        when(value.get()).thenReturn("Test");
        when(l2.get("Test")).thenReturn(value).thenReturn(null);
        when(l1.get("Test")).thenReturn(null).thenReturn(value);

        CacheMessagePusher pusher = mock(CacheMessagePusher.class);
        SecondaryCacheProperties secondaryCacheProperties = new SecondaryCacheProperties();
        secondaryCacheProperties.setCacheNullValues(true);
        secondaryCacheProperties.setLevel2Enabled(true);
        SecondaryCache cache = new SecondaryCache("Test",l1,l2,secondaryCacheProperties,pusher);
        assertTrue(cache.isAllowNullValues());
        assertEquals("Test",cache.getName());
        assertNotNull(cache.getIdentifier());
        assertEquals(l1,cache.getNativeCache());

        assertEquals("Test",cache.get("Test").get());
        assertEquals("Test",cache.get("Test").get());

        assertEquals("Test1",cache.get("Test1",()->"Test1"));
        assertEquals("Test1",cache.get("Test1",()->"Test1"));
        assertThrows(IllegalStateException.class,()->cache.get("Test1",()-> {throw new Exception();}));
        cache.evict("Test");
        cache.clearLocal("Test");
        cache.clearLocal(null);
        cache.clear();
        assertEquals(0, NativeCaches.size(cache));
        assertEquals(0,NativeCaches.keys(cache).size());
        assertEquals(0,NativeCaches.values(cache).size());

        cache.put("Test","exec");
        cache.putIfAbsent("Test","Test");
        cache.putIfAbsent("Test",null);

        secondaryCacheProperties.setCacheNullValues(false);
        SecondaryCache cache1 = new SecondaryCache("Test",l1,l2,secondaryCacheProperties,pusher);
        assertNull(cache1.putIfAbsent("Test",null));
        cache1.put("Test",null);

        Cache l11 = mock(Cache.class);
        Cache l22 = mock(Cache.class);
        when(l22.get("Test")).thenReturn(value);
        when(l11.get("Test")).thenReturn(value);
        SecondaryCache cache2 = new SecondaryCache("Cache2",l11,l22,secondaryCacheProperties,pusher);
        cache2.close(0);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(1);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(2);
        assertNull(cache2.get("Test"));
        cache2.open(0);
        assertNull(cache2.get("Test"));
        cache2.open(2);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(2);
        assertNull(cache2.get("Test"));
        cache2.open(1);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(1);
        assertNull(cache2.get("Test"));
        when(l22.get("Test")).thenReturn(value);
        when(l11.get("Test")).thenReturn(value).thenReturn(null);
        cache2.open(9);
        assertEquals(value.get(),cache2.get("Test").get());
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(9);
        assertNull(cache2.get("Test"));
        assertNull(cache2.get("Test"));

        Cache only = mock(Cache.class);
        SecondaryCache cache3 = SecondaryCache.onlyCache1("Cache3", only,secondaryCacheProperties, null);
        cache3.evict("Test");
        cache3.clear();

        cache3 = SecondaryCache.onlyCache2("Cache3", only,secondaryCacheProperties, null);
        cache3.evict("Test");
        cache3.clear();
    }
}