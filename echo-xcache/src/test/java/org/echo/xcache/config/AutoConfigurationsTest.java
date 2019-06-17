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

package org.echo.xcache.config;

import org.apache.commons.lang3.reflect.MethodUtils;
import org.echo.redis.config.RedisBaseComponentConfiguration;
import org.echo.xcache.XCacheProperties;
import org.echo.xcache.binary.BinaryCacheManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.Method;

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
@DisplayName("Echo : xCache AutoCacheConfigurations Test")
class AutoConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private BinaryCacheManager binaryCacheManager;

    @Autowired
    private KeyGenerator keyGenerator;

    @Autowired
    private XCacheProperties cacheProperties;

    @Test
    public void test(){
        assertTrue(cacheManager instanceof CompositeCacheManager);
        assertTrue(binaryCacheManager.hasTwoLevel());
        Cache c1 = cacheManager.getCache("echo:test:A");
        c1.put("A","AA");
        assertNotNull(c1.get("A"));
        c1.evict("A");
        TestKeyGenerator generator = new TestKeyGenerator();
        Method method = MethodUtils.getAccessibleMethod(TestKeyGenerator.class, "getKey");
        Object o = keyGenerator.generate(generator, method,"A","123");
        assertNotNull(o);

        String cn1 = cacheProperties.getCacheName("echo:test:A");
        assertEquals("echo:test:A",cn1);
        String cn2 = cacheProperties.getCacheName("A");
        assertEquals("echo:test:A",cn2);
    }
}