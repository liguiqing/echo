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

package org.echo.xcache;

import lombok.extern.slf4j.Slf4j;
import org.echo.xcache.binary.BinaryCache;
import org.echo.xcache.caffeine.CaffeineCaches;
import org.echo.xcache.redis.XRedisCache;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;

/**
 * 缓存工具类
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class NativeCaches {
    private NativeCaches() {
        throw new AssertionError("No NativeCaches instances for you!");
    }

    private static boolean isCaffeineCache(Cache cache){
        return (cache instanceof CaffeineCache);
    }

    private static boolean isXRedisCache(Cache cache){
        return (cache instanceof XRedisCache);
    }

    public static int size(Cache cache) {

        Cache nativeCache = toNativeCache(cache);
        if (Objects.isNull(cache))
            return 0;

        if(isCaffeineCache(nativeCache)){
            return CaffeineCaches.size((CaffeineCache)nativeCache);
        }

        if(isXRedisCache(nativeCache)){
            XRedisCache xRedisCache = (XRedisCache) nativeCache;
            return xRedisCache.size();
        }

        return 0;
    }

    public static Set keys (Cache cache){
        Cache nativeCache = toNativeCache(cache);
        if(isCaffeineCache(nativeCache)){
            return CaffeineCaches.keys((CaffeineCache)nativeCache);
        }

        if(isXRedisCache(nativeCache)){
            XRedisCache xRedisCache = (XRedisCache) nativeCache;
            return xRedisCache.keys();
        }
        return Collections.emptySet();
    }

    public static Collection values(Cache cache){
        Cache nativeCache = toNativeCache(cache);
        if(isCaffeineCache(nativeCache)){
            return CaffeineCaches.values((CaffeineCache)nativeCache);
        }

        if(isXRedisCache(nativeCache)){
            XRedisCache xRedisCache = (XRedisCache) nativeCache;
            return xRedisCache.values();
        }

        return Collections.emptySet();
    }

    private static Cache toNativeCache(Cache cache){
        if(cache == null)
            return null;
        if(cache instanceof BinaryCache)
            return (Cache)cache.getNativeCache();
        return cache;
    }
}