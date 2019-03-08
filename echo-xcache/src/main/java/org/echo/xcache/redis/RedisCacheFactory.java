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
import org.echo.xcache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author Liguiqing
 * @since V.0
 */
@Slf4j
public class RedisCacheFactory implements CacheFactory {

    private RedisCacheProperties cacheProperties;

    private JedisPool jedisPool;

    private RedisTemplate<Object,Object> redisTemplate;

    public RedisCacheFactory(RedisCacheProperties cacheProperties, JedisPool jedisPool, RedisTemplate<Object, Object> redisTemplate) {
        this.cacheProperties = cacheProperties;
        this.jedisPool = jedisPool;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Cache newCache(String name) {
        return this.newCache(name, -1, -1);
    }

    @Override
    public Cache newCache(String name, long ttl, long maxIdleSecond) {
        if(isRedisConnected()){
            return new XRedisCache(name, this.cacheProperties.getCachePrefix(), cacheProperties.isCacheNullValues(),
                    cacheProperties.getTtl(name,ttl), this.redisTemplate, this.jedisPool);
        }
        return new RedisNoneCache(false);
    }

    private boolean isRedisConnected(){
        return jedisPool.getResource().isConnected();
    }
}