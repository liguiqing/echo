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

package org.echo.spring.cache.redis;


import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;
import redis.clients.jedis.JedisPool;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * <p>
 * 扩展RedisCache
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-05 13:42
 * @since V1.0.0
 **/
@Slf4j
public class XRedisCache extends AbstractValueAdaptingCache {

    private String name;

    private long expire; //过期时间：ms

    private RedisTemplate<Object, Object> redisTemplate;

    private String cachePrefix;

    private JedisPool jedisPool;

    /**
     *
     * @param name of cache
     * @param cachePrefix of cache name
     * @param allowNullValues of ull value stored if true,otherwise ignore null value
     * @param expire second of value ,-1 never expired
     * @param redisTemplate spring RedisTemplate
     * @param jedisPool JedisPool
     */
    public XRedisCache(String name, String cachePrefix,
                          boolean allowNullValues,long expire,
                         RedisTemplate<Object, Object> redisTemplate,
                          JedisPool jedisPool) {
        super(allowNullValues);
        this.name = name;
        this.expire = expire * 1000;
        this.cachePrefix = cachePrefix;
        this.redisTemplate = redisTemplate;
        this.jedisPool = jedisPool;
    }

    @Override
    protected Object lookup(Object key) {
        log.debug("Lookup Object from redis by key [{}]",key);
        Object cacheKey = getKey(key);
        return redisTemplate.opsForValue().get(cacheKey);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        Object o = this.lookup(key);
        if(o == null){
            try {
                return (T)this.putIfAbsent(key,valueLoader.call()).get();
            } catch (Exception e) {
                log.error(ThrowableToString.toString(e));
                return null;
            }
        }

        jedisPool.getResource().expireAt(getKey(key).toString(),this.expire);
        return (T)o;
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("Put Object to redis [{}] -> [{}]",key,value);

        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }

        storeValue(key,value);
    }

    @Override
    public Cache.ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put Object to redis [{}] -> [{}] if it absent",key,value);

        Object cacheKey = getKey(key);
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if(cacheValue == null) {
            this.put(key,value);
        }
        return toValueWrapper(value);
    }

    private void storeValue(Object key,Object value){
        if (expire > 0) {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value), this.expire, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value));
        }
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict Object from redis [{}]",key);

        redisTemplate.delete(getKey(key));
    }

    @Override
    public void clear() {
        log.debug("Clear redis store from by key prefix:{}",this.name.concat(":"));

        redisTemplate.delete(keys());
    }

    private Object getKey(Object key) {
        return getCachePrefix().concat(key.toString());
    }

    private String getCachePrefix(){
        return (StringUtils.isEmpty(cachePrefix)?"echo":cachePrefix).concat(":").concat(this.name).concat(":");
    }

    public Set<Object> keys(){
        return redisTemplate.keys(getCachePrefix().concat("*"));
    }

    public Collection<Object> values(){
        return redisTemplate.opsForValue().multiGet(keys());
    }

    public int size(){
        return keys().size();
    }
}
