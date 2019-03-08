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
import org.echo.xcache.config.RedisCacheConfigurations;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.*;


@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisCacheConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@Slf4j
@DisplayName("Echo : xCache RedisCacheProperties Test")
class RedisCachePropertiesTest extends AbstractConfigurationsTest {

    @Autowired
    RedisCacheProperties properties;

    @Test
    void test(){
        assertTrue(true);
        assertNull(properties.getName());
        assertEquals(360000l,properties.getDefaultExpiration());
        assertEquals("echo",properties.getCachePrefix());
        assertEquals(1,properties.getHostsAndPorts().size());
        assertEquals(2,properties.getExpires().size());
        assertEquals(6000,properties.getTtl("cache1",0));
        assertEquals(5000,properties.getTtl("cache2",0));
        assertEquals(0,properties.getTtl("cache3",0));
        assertEquals("echo:cache",properties.getCacheName("cache"));
        assertEquals("echo:cache",properties.getCacheName("echo:cache"));
        assertEquals("192.168.1.251",properties.getStandalone().getHost());
        assertEquals(9999,properties.getStandalone().getPort());
    }
}