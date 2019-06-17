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

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.echo.xcache.XCacheProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Slf4j
@DisplayName("Echo : xCache RedisCacheFactory Test")
class RedisCacheFactoryTest {

    @Mock
    private XCacheProperties redisCacheProperties;

    @Mock
    private RedisTemplate template;


    @BeforeEach
    void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void newCache() {

        Map<String,Long> expires = Maps.newHashMap();
        expires.put("Test", 1000L);
        expires.put("Test1", 1000L);
        expires.put("Test2", 1000L);

        redisCacheProperties.setExpires(expires);
        RedisCacheFactory redisCacheFactory  = new RedisCacheFactory(redisCacheProperties,template);

        Cache cache1 = redisCacheFactory.newCache("Test11",1,1);
        assertNotNull(cache1);
        cache1 = redisCacheFactory.newCache("Test2");
        assertNotNull(cache1);

        cache1 = redisCacheFactory.newCache("Test3",1,1);
        assertNotNull(cache1);
    }
}