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

package org.echo.xcache;

import org.echo.lock.DistributedLock;
import org.echo.messaging.MessagePublish;
import org.echo.redis.config.RedisBaseComponentConfiguration;
import org.echo.util.ClassUtils;
import org.echo.xcache.binary.BinaryCache;
import org.echo.xcache.caffeine.CaffeineCaches;
import org.echo.xcache.caffeine.CaffeineProperties;
import org.echo.xcache.config.AutoCacheConfigurations;
import org.echo.xcache.redis.RedisNoneCache;
import org.echo.xcache.redis.XRedisCache;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@ExtendWith(SpringExtension.class)
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisAutoConfiguration.class,
                RedissonAutoConfiguration.class,
                RedisBaseComponentConfiguration.class,
                AutoCacheConfigurations.class
        })
)
@DisplayName("NativeCaches Test")
class NativeCachesTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private XCacheProperties xCacheProperties;

    @Autowired
    private RedisTemplate<Object,Object> template;


    @Test
    void test() {
        assertEquals(0,NativeCaches.size(null));
        assertEquals(0,NativeCaches.size(new RedisNoneCache(true)));
        CacheTestKey k1 = new CacheTestKey("k1");
        CacheTestKey k2 = new CacheTestKey("k2");
        CacheTestValue v1 = new CacheTestValue().setV1("v1").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheTestValue v2 = new CacheTestValue().setV1("v2").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheTestValue v11 = new CacheTestValue().setV1("v11").setV2(true).setV3(11);
        v1.addChild(v11);

        CaffeineCache  caffeineCache = new CaffeineCache("exec",CaffeineCaches.newCache(new CaffeineProperties()));
        assertEquals(0,NativeCaches.size(caffeineCache));
        caffeineCache.put(k1,v1);
        assertEquals(1,NativeCaches.size(caffeineCache));
        assertTrue(NativeCaches.keys(caffeineCache).contains(k1));
        assertFalse(NativeCaches.keys(caffeineCache).contains(k2));
        assertTrue(NativeCaches.values(caffeineCache).contains(v1));
        assertFalse(NativeCaches.values(caffeineCache).contains(v2));

        template.delete("echo:Test:*");
        XRedisCache xcache = new XRedisCache("Test", "echo", false, 30, template);

        assertEquals(0,NativeCaches.size(xcache));
        xcache.put(k1,v1);
        assertNotNull(xcache.get(k1));
        assertEquals(1,NativeCaches.size(xcache));
        assertTrue(NativeCaches.keys(xcache).contains("echo:Test:"+k1));
        assertFalse(NativeCaches.keys(xcache).contains(k2));
        assertTrue(NativeCaches.values(xcache).contains(v1));
        assertFalse(NativeCaches.values(xcache).contains(v2));
        xcache.evict(k1);
        template.delete("echo:Test:" + k1);
        DistributedLock lock = mock(DistributedLock.class);

        BinaryCache sCache = BinaryCache.onlyCache1("exec", caffeineCache, xCacheProperties, lock);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        sCache = BinaryCache.onlyCache2("exec", caffeineCache, xCacheProperties, lock);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        MessagePublish pusher = mock(MessagePublish.class);
        sCache = new BinaryCache("exec", caffeineCache,xcache, xCacheProperties,pusher);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        Cache noneCache = new RedisNoneCache(true);
        assertEquals(0,NativeCaches.size(noneCache));
        assertFalse(NativeCaches.keys(noneCache).contains(k1));
        assertFalse(NativeCaches.keys(noneCache).contains(k2));
        assertFalse(NativeCaches.values(noneCache).contains(v1));
        assertFalse(NativeCaches.values(noneCache).contains(v2));

        assertEquals(0,NativeCaches.keys(null).size());
        assertEquals(0,NativeCaches.values(null).size());
        assertThrows(Exception.class,() -> ClassUtils.newInstanceOf(NativeCaches.class));
    }
}