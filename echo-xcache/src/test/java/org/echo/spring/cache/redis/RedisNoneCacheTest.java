package org.echo.spring.cache.redis;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("RedisNoneCache Test")
class RedisNoneCacheTest {

    @Test
    void lookup() {
        assertNull(new RedisNoneCache(false).lookup(""));
    }

    @Test
    void getName() {
        assertNull(new RedisNoneCache(false).getName());
    }

    @Test
    void getNativeCache() {
        assertNull(new RedisNoneCache(false).getNativeCache());
    }

    @Test
    void get() {
        assertNull(new RedisNoneCache(false).get(""));
        assertNull(new RedisNoneCache(false).get("",()->""));
    }

    @Test
    void put() {
        new RedisNoneCache(false).put("","");
    }

    @Test
    void putIfAbsent() {
        assertNull(new RedisNoneCache(false).putIfAbsent("",""));
    }

    @Test
    void evict() {
       new RedisNoneCache(false).evict("");
    }

    @Test
    void clear() {
        new RedisNoneCache(false).clear();
    }
}