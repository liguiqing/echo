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

package org.echo.xcache.caffeine;

import org.assertj.core.util.Lists;
import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DisplayName("CaffeineCacheFactory Test")
class CaffeineCacheFactoryTest {
    @Test
    void test(){

        CaffeineCacheProperties ccp = new CaffeineCacheProperties();
        CaffeineProperties cp = new CaffeineProperties().setName("Test");
        ccp.setDefaultProp(cp);
        assertEquals("Test",cp.getName());
        assertEquals("Test",ccp.getName());
        CaffeineCacheFactory factory = new CaffeineCacheFactory(ccp);
        CaffeineCache cache = (CaffeineCache)factory.newCache("Test");
        assertEquals("Test",cache.getName());
        assertEquals(cp,ccp.getProp("Test"));

        ArrayList<CaffeineProperties> boot = Lists.newArrayList();
        boot.add(new CaffeineProperties().setName("Test1"));
        boot.add(new CaffeineProperties().setName("Test2").setExpireAfterWrite(2000));
        ccp.setCachesOnBoot(boot);
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        ccp.setCachesOnBoot(Lists.newArrayList());
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        boot = Lists.newArrayList();
        boot.add(new CaffeineProperties().setName("Test2").setExpireAfterWrite(2000));
        ccp.setCachesOnBoot(boot);
        cache = (CaffeineCache)factory.newCache("Test1");
        assertEquals("Test1",cache.getName());

        assertThrows(Exception.class,()->
                new PrivateConstructors().exec(CaffeineCaches.class)
        );
    }
}