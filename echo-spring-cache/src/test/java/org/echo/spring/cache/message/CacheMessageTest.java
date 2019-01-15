package org.echo.spring.cache.message;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("CacheMessage Test")
class CacheMessageTest {
    @Test
    void isClosed() {
        assertFalse(new CacheMessage("id","name","key").isClosed());
        assertTrue(new CacheMessage("id","name","key",1).isClosed());
    }

    @Test
    void isOpen() {
        assertFalse(new CacheMessage("id","name","key").isOpen());
        assertTrue(new CacheMessage("id","name","key",-1).isOpen());
    }

    @Test
    void sameOfIdentifier() {
        CacheMessage message = new CacheMessage("id","name","key");
        assertTrue(message.sameOfIdentifier("id"));
        assertEquals("name",message.getCacheName());
        assertEquals("id",message.getIdentifier());
        assertEquals(0,message.getClosedLevel());
        assertEquals("key",message.getKey());

        assertEquals(message,new CacheMessage("id","name","key"));
        assertTrue(new CacheMessage("id","name","key").toString().contains("id"));
    }
}