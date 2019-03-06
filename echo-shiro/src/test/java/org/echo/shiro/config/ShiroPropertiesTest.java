package org.echo.shiro.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("ShiroProperties Test")
class ShiroPropertiesTest {

    @Test
    void getCacheName() {
        ShiroProperties sp = new ShiroProperties();
        assertEquals("exec#"+sp.getMaxIdleSecond()+"#"+sp.getMaxIdleSecond()+"#1",sp.getCacheName("exec"));
        sp.setMaxIdleSecond(1);
        assertEquals("exec#"+sp.getMaxIdleSecond()+"#"+sp.getMaxIdleSecond()+"#1",sp.getCacheName("exec"));
        HashMap<String,Long> expires = new HashMap<>();
        expires.put("test1", 1L);
        expires.put("test2", 0L);
        expires.put("test3", -1L);
        sp.setCacheDefaults(expires);
        assertEquals("test1#1#1#1",sp.getCacheName("test1"));
        assertEquals("test2#0#0#1",sp.getCacheName("test2"));
        assertEquals("test3#-1#-1#1",sp.getCacheName("test3"));
        assertEquals("test4#1#1#1",sp.getCacheName("test4"));
    }
}