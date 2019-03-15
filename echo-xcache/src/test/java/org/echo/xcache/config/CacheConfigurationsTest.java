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
import org.echo.test.config.AbstractConfigurationsTest;
import org.echo.xcache.CacheDequeFactory;
import org.echo.xcache.CacheTestValue;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RedissonClient;

import java.lang.reflect.Method;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@DisplayName("Echo : xCache Configurations Test")
class CacheConfigurationsTest extends AbstractConfigurationsTest {

    @Test
    public void cacheDequeFactory()throws Exception{
        RedisCacheConfigurations scc = new RedisCacheConfigurations();
        CacheDequeFactory cacheDequeFactory = scc.cacheDequeFactory(null);
        assertNotNull(cacheDequeFactory);
        assertNotNull(cacheDequeFactory.getDeque(""));
        RedissonClient client = mock(RedissonClient.class);
        RAtomicLong rAtomicLong = mock(RAtomicLong.class);
        when(client.getAtomicLong(any(String.class))).thenReturn(rAtomicLong);
        assertNotNull(scc.cacheDequeFactory(client));

        CacheTestValue v1 = new CacheTestValue().setV1("v1").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheConfigurations cs = new CacheConfigurations();
        Method m = v1.getClass().getMethod("getV1");
        assertEquals("org.echo.xcache.CacheTestValuegetV1",cs.keyGenerator().generate(v1,m));
        assertEquals("org.echo.xcache.CacheTestValuegetV1ab",cs.keyGenerator().generate(v1,m,"a","b"));
    }
}