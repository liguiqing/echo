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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("RedisNoneCache Test")
class RedisNoneCacheTest {

    @Test
    void lookup() {
        assertNull(new RedisNoneCache(false).lookup(""));
    }

    @Test
    void getName() {
        assertNull(new RedisNoneCache(false).getName());
    }

    @Test
    void getNativeCache() {
        assertNull(new RedisNoneCache(false).getNativeCache());
    }

    @Test
    void get() {
        assertNull(new RedisNoneCache(false).get(""));
        assertNull(new RedisNoneCache(false).get("",()->""));
    }

    @Test
    void put() {
        new RedisNoneCache(false).put("","");
    }

    @Test
    void putIfAbsent() {
        assertNull(new RedisNoneCache(false).putIfAbsent("",""));
    }

    @Test
    void evict() {
       new RedisNoneCache(false).evict("");
    }

    @Test
    void clear() {
        new RedisNoneCache(false).clear();
    }
}