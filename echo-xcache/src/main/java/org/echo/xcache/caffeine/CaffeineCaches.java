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

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class CaffeineCaches {

    private CaffeineCaches() {
        throw new AssertionError("No CaffeineCaches instances for you!");
    }

    public static Cache newCache(CaffeineProperties properties) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(properties.getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(properties.getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }

        if(properties.getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(properties.getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(properties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(properties.getInitialCapacity());
        }
        if(properties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(properties.getMaximumSize());
        }
        return cacheBuilder.build();
    }

    public static int size(CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return Long.valueOf(nativeCache.estimatedSize()).intValue();
    }

    public static Set keys (CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return nativeCache.asMap().keySet();
    }

    public static Collection values (CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return nativeCache.asMap().values();
    }
}