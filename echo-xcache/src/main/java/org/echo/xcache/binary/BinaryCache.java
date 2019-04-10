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

package org.echo.xcache.binary;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.lock.DistributedLock;
import org.echo.xcache.message.CacheMessage;
import org.echo.xcache.message.CacheMessagePusher;
import org.springframework.cache.Cache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.Callable;

/**
 * 两级缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@EqualsAndHashCode(of = {"name"})
@ToString(of = {"name","identifier"})
public class BinaryCache extends AbstractValueAdaptingCache {

    @Getter
    private String identifier = UUID.randomUUID().toString();

    @Getter
    private String name;

    private Cache cacheL1;

    private Cache cacheL2;

    private String topic;

    private CacheMessagePusher messagePusher;

    private DistributedLock<Object, Object> lock;

    private Cache cacheL1Bak;

    private Cache cacheL2Bak;

    public BinaryCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    public BinaryCache(String name, Cache cacheL1, Cache cacheL2,
                       BinaryCacheProperties cacheProperties,
                       CacheMessagePusher messagePusher) {
        this(name, cacheL1, cacheL2, cacheProperties, messagePusher, new DistributedLock() {});
    }

    public BinaryCache(String name, Cache cacheL1, Cache cacheL2,
                       BinaryCacheProperties cacheProperties,
                       CacheMessagePusher messagePusher, DistributedLock lock) {

        this(cacheProperties.isCacheNullValues());
        this.name = name;
        this.cacheL1 = cacheL1;
        this.topic = cacheProperties.getCacheMessageTopic();
        this.messagePusher = messagePusher;
        if (cacheProperties.isLevel2Enabled()) {
            this.cacheL2 = cacheL2;
        }
        this.lock = lock;
    }

    public static BinaryCache onlyCache1(String name, Cache cacheL1, BinaryCacheProperties cacheProperties, DistributedLock<Object, Object> lock) {
        BinaryCache cache = cacheOf(name, cacheProperties, lock);
        cache.cacheL1 = cacheL1;
        return cache;
    }

    public static BinaryCache onlyCache2(String name, Cache cacheL2, BinaryCacheProperties cacheProperties, DistributedLock<Object, Object> lock) {
        BinaryCache cache = cacheOf(name, cacheProperties, lock);
        cache.cacheL2 = cacheL2;
        return cache;
    }

    private static BinaryCache cacheOf(String name, BinaryCacheProperties cacheProperties, DistributedLock<Object, Object> lock) {
        BinaryCache cache = new BinaryCache(cacheProperties.isCacheNullValues());
        cache.name = name;
        cache.lock = lock == null ? new DistributedLock<Object, Object>() {} : lock;
        return cache;
    }

    @Override
    public Object getNativeCache() {
        return this.cacheL1 == null ? this.cacheL2 : this.cacheL1;
    }

    @Override
    protected Object lookup(Object key) {
        log.debug("Lookup Object from cache [{}] by key [{}]", this.name, key);

        Object value = getFromLevel1(key);
        if (value != null) {
            return value;
        }

        if (!hasCache2())
            return null;

        value = getFromLevel2(key);

        if (value != null) {
            putToLevel1(key, value);
        }
        return value;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("Get Object from cache [{}] by key [{}]", this.name, key);

        Object value = lookup(key);
        if (value != null) {
            return (T) value;
        }

        value = lock.lock(key, () -> {
            Object newValue = valueLoader.call();
            putToLevel1(key, newValue);
            putToLevel2(key, newValue);
            return newValue;
        });
        return (T) value;
    }

    @Override
    public void put(Object key, Object value) {
        log.debug("Put cache of {}->{} into cache [{}]", key, value, this.name);

        if (!super.isAllowNullValues() && value == null) {
            //不允许为null时,put null 将清空缓存
            this.evict(key);
            return;
        }
        putToLevel1(key, value);
        putToLevel2(key, value);
        push(new CacheMessage(identifier, this.name, key));
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put cache if absent of {}->{}", key, value);

        if (!super.isAllowNullValues() && value == null) {
            return null;
        }

        ValueWrapper valueWrapper = this.get(key);
        if (valueWrapper != null && valueWrapper.get() != null)
            return valueWrapper;
        try {
            lock.lock(key, () -> {
                this.put(key, value);
                return value;
            });
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        }
        return this.get(key);
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict by key [{}] from cache [{}]", key, this.name);

        if (this.hasCache2()) {
            this.cacheL2.evict(key);
        }
        if (this.hasCache1())
            this.cacheL1.evict(key);
        push(new CacheMessage(identifier, this.name, key));
    }

    @Override
    public void clear() {
        log.debug("Clear cache [{}]", this.name);
        if (hasCache1())
            this.cacheL1.clear();
        if (this.hasCache2()) {
            this.cacheL2.clear();
        }
        push(new CacheMessage(identifier, this.name, null));
    }

    private Object getFromLevel1(Object key) {
        if (!hasCache1())
            return null;

        ValueWrapper value = cacheL1.get(key);
        if (value != null) {
            log.debug("Hits by key [{}] from level1 in cache [{}]", key, this.name);
            return value.get();
        }

        log.debug("Don't hit by key [{}] from level1 in cache [{}]", key, this.name);
        return null;
    }

    private void putToLevel1(Object key, Object value) {
        if (!hasCache1())
            return;

        log.debug("Put cache by key {} into level1 of cache [{}]", key, this.name);
        this.cacheL1.put(key, value);
    }

    private Object getFromLevel2(Object key) {
        if (this.hasCache2()) {
            Object o = this.cacheL2.get(key);

            if (o != null) {
                log.debug("Hit by key [{}] from level2 in cache [{}]", key, this.name);
                return (o instanceof ValueWrapper) ? ((ValueWrapper) o).get() : o;
            } else {
                log.debug("Don't hit by key [{}] from level2 in cache [{}]", key, this.name);
            }
        }
        return null;
    }

    private void putToLevel2(Object key, Object value) {
        if (this.hasCache2()) {
            log.debug("Put cache by key [{}] into level2 of cache [{}]", key, this.name);
            cacheL2.put(key, value);
        }
    }

    private boolean hasCache1() {
        return this.cacheL1 != null;
    }

    private boolean hasCache2() {
        return this.cacheL2 != null;
    }

    /**
     * 缓存变更时通知其他节点清理一级缓存
     *
     * @param message a message to Redis server
     */
    private void push(CacheMessage message) {
        if (!Objects.isNull(this.messagePusher)) {
            log.debug("Push a cache update message");
            this.messagePusher.push(topic, message);
        }
    }


    /**
     * 清理本地缓存
     *
     * @param key key of cache what's be cleared; clean level1 if key is null
     */
    protected void clearLocal(Object key) {
        if (!hasCache1())
            return;

        if (key != null) {
            log.debug("Clear  level1 by key [{}] in cache [{}]", key, this.name);
            this.cacheL1.evict(key);
        } else {
            log.debug("Clear  level1 of cache [{}]", this.name);
            this.cacheL1.clear();
        }
    }

    protected void close(int level) {
        if (level == 1) {
            closeL1();
        } else if (level == 2) {
            closeL2();
        } else if (level == 9) {
            closeL1();
            closeL2();
        }
    }

    private void closeL1() {
        if (this.hasCache1()) {
            this.cacheL1.clear();
            this.cacheL1Bak = this.cacheL1;
            this.cacheL1 = null;
            log.debug("Cache {} L1 closed !", this.name);
        }
    }

    private void closeL2() {
        if (this.hasCache2()) {
            this.cacheL2.clear();
            this.cacheL2Bak = cacheL2;
            this.cacheL2 = null;
            log.debug("Cache {} L2 closed !", this.name);
        }
    }

    protected void open(int level) {
        if (level == 9) {
            openL1();
            openL2();
        } else if (level == 1) {
            openL1();
        } else if (level == 2) {
            openL2();
        }
    }

    private void openL1() {
        if (!hasCache1()) {
            this.cacheL1 = this.cacheL1Bak;
            this.cacheL1Bak = null;
            log.debug("Cache {} L1 opened !", this.name);
        }
    }

    private void openL2() {
        if (!hasCache2()) {
            this.cacheL2 = this.cacheL2Bak;
            this.cacheL2Bak = null;
            log.debug("Cache {} L2 opened !", this.name);
        }
    }

}