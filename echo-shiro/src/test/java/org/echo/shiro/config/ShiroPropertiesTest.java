package org.echo.shiro.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("ShiroPropertiesTest")
class ShiroPropertiesTest {

    @Test
    void getCacheName() {
        ShiroProperties sp = new ShiroProperties();
        assertEquals("test#"+sp.getMaxIdleSecond()+"#"+sp.getMaxIdleSecond()+"#2",sp.getCacheName("test"));
        sp.setMaxIdleSecond(1);
        assertEquals("test#"+sp.getMaxIdleSecond()+"#"+sp.getMaxIdleSecond()+"#2",sp.getCacheName("test"));
        HashMap<String,CacheProperties> expires = new HashMap<>();
        expires.put("test1", new CacheProperties("test1",1));
        expires.put("test2", new CacheProperties("test2",0));
        expires.put("test3", new CacheProperties("test3",-1));
        sp.setCachePropertiesMap(expires);
        assertEquals("test1#1#1#2",sp.getCacheName("test1"));
        assertEquals("test2#0#0#2",sp.getCacheName("test2"));
        assertEquals("test3#-1#-1#2",sp.getCacheName("test3"));
        assertEquals("test4#1#1#2",sp.getCacheName("test4"));
    }
}