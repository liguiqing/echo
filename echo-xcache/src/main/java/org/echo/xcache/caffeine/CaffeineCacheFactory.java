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

import org.echo.xcache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 *  Caffeine cache Factory
 *
 * @author Liguiqing
 * @since V1.0
 */

public class CaffeineCacheFactory implements CacheFactory {

    private CaffeineCacheProperties caffeineCacheProperties;

    public CaffeineCacheFactory(CaffeineCacheProperties caffeineCacheProperties) {
        this.caffeineCacheProperties = caffeineCacheProperties;
    }

    @Override
    public Cache newCache(String name) {
        return new CaffeineCache(name,CaffeineCaches.newCache(this.caffeineCacheProperties.getProp(name)));
    }

    @Override
    public Cache newCache(String name, long ttl, long maxIdleSecond) {
        CaffeineProperties cp = new CaffeineProperties();
        cp.setExpireAfterWrite(ttl * 1000)
                .setExpireAfterAccess(maxIdleSecond * 1000);
        return new CaffeineCache(name,CaffeineCaches.newCache(cp));
    }
}