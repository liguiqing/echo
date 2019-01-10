package org.echo.spring.cache.secondary;

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
        assertEquals(0,cache.size());
        assertEquals(0,cache.keys().size());
        assertEquals(0,cache.values().size());

        cache.put("Test","test");
        cache.putIfAbsent("Test","Test");
        cache.putIfAbsent("Test",null);

        secondaryCacheProperties.setCacheNullValues(false);
        SecondaryCache cache1 = new SecondaryCache("Test",l1,l2,secondaryCacheProperties,pusher);
        assertNull(cache1.putIfAbsent("Test",null));
        cache1.put("Test",null);
    }
}