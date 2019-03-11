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

import lombok.extern.slf4j.Slf4j;
import org.echo.xcache.binary.BinaryCacheManager;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                AutoCacheConfigurations.class
        }))
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@Slf4j
@DisplayName("Echo : xCache AutoCacheConfigurations Test")
class AutoConfigurationsTest  extends AbstractConfigurationsTest {

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private BinaryCacheManager binaryCacheManager;

    @Test
    public void test(){
        assertTrue(cacheManager instanceof CompositeCacheManager);
        assertTrue(binaryCacheManager.hasTwoLevel());
    }
}