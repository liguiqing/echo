/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.echo.xcache.redis;

import lombok.extern.slf4j.Slf4j;
import org.echo.redis.config.RedisBaseComponentConfiguration;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisAutoConfiguration.class,
                RedissonAutoConfiguration.class,
                RedisBaseComponentConfiguration.class,
                AutoCacheConfigurations.class
        })
@Slf4j
@DisplayName("Echo : xCache XRedisCache Test")
class XRedisCacheTest {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;

    @Test
    void test(){
        assertTrue(true);

        XRedisCache cache1 = new XRedisCache("Test", "echo", false, 3, redisTemplate);
        assertEquals("Test",cache1.getName());
        assertEquals(cache1,cache1.getNativeCache());
        cache1.put("A","a:A");
        assertEquals("a:A",cache1.get("A").get());
        await().atMost(4, SECONDS).until(()->cache1.get("A"),nullValue());
        assertEquals("a:A",cache1.get("A",()->"a:A"));
        await().atMost(2, SECONDS).until(()->cache1.get("A",()->"a:A"),is("a:A"));
        await().atMost(1, SECONDS).until(()->cache1.get("A",()->"a:A"),is("a:A"));
        await().atMost(3, SECONDS).until(()->cache1.get("A",()->"a:A"),is("a:A"));
        await().atMost(4, SECONDS).until(()->cache1.get("A"),nullValue());

        cache1.putIfAbsent("A", "a:A");
        await().atMost(4, SECONDS).until(()->cache1.get("A"),nullValue());
        cache1.putIfAbsent("B", "b:A");
        await().atMost(2, SECONDS).until(()->cache1.get("B").get(),is("b:A"));
        cache1.putIfAbsent("C", "c:A");
        assertEquals("c:A",cache1.get("C").get());
        cache1.putIfAbsent("C", null);
        assertEquals("c:A",cache1.get("C").get());
        cache1.put("C", null);
        assertNull(cache1.get("C"));
        cache1.evict("B");
        assertNull(cache1.get("B"));
        cache1.putIfAbsent("B", "b:A");
        cache1.putIfAbsent("C", "c:A");
        assertEquals("b:A",cache1.get("B").get());
        assertEquals("c:A",cache1.get("C").get());
        cache1.clear();
        assertNull(cache1.get("B"));
        assertNull(cache1.get("C"));

        XRedisCache cache2 = new XRedisCache("Test2", "echo", false, 0, redisTemplate);
        cache2.put("A","a:A");
        assertEquals("a:A",cache2.get("A").get());

        await().atMost(4, SECONDS).until(()->cache2.get("A").get(),is("a:A"));
        await().atMost(5, SECONDS).until(()->cache2.get("A").get(),is("a:A"));

        XRedisCache cache3 = new XRedisCache("Test3", "echo", true, 0, redisTemplate);
        cache3.put("A","a:A");
        assertEquals("a:A",cache3.get("A").get());
        cache3.put("A",null);
        assertNull(cache3.get("A").get());
        cache3.get("D",()->(1/0));
        redisTemplate.delete("echo:Test2:A");
        redisTemplate.delete("echo:Test3:A");
    }
}