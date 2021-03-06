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
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisNoneCache extends AbstractValueAdaptingCache {

    public RedisNoneCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    protected Object lookup(Object key) {
        warn();
        return null;
    }

    @Override
    public String getName() {
        warn();
        return null;
    }

    @Override
    public Object getNativeCache() {
        return getName();
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        warn();
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        warn();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        warn();
        return null;
    }

    @Override
    public void evict(Object key) {
        warn();
    }

    @Override
    public void clear() {
        warn();
    }

    private void  warn(){
        log.warn("Redis has some problem,please check the server can be access!");
    }
}