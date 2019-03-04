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

package org.echo.shiro.cache;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("SpringCache Test")
class SpringCacheTest {

    @Test
    void test(){
        assertTrue(true);
        org.springframework.cache.Cache cache = mock(org.springframework.cache.Cache.class);
        SpringCache cache1 = new SpringCache(cache);
        when(cache.get(any())).thenReturn(null).thenReturn(()->"a");
        assertNull(cache1.get("A"));
        assertEquals("a",cache1.get("A"));
        when(cache.putIfAbsent(any(),any())).thenReturn(()->"a");
        assertEquals("a",cache1.put("a","A"));
        assertEquals("a",cache1.remove("a"));
        cache1.clear();
        assertEquals(0,cache1.size());
        assertEquals(0,cache1.keys().size());
        assertEquals(0,cache1.values().size());
    }
}