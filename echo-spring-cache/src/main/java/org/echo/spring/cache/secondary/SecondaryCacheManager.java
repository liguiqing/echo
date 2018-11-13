package org.echo.spring.cache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * 两级缓存管理器
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class SecondaryCacheManager extends AbstractTransactionSupportingCacheManager {

    private SecondaryCacheProperties cacheProperties;

    private boolean dynamic;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheFactory cacheL1Factory;

    private CacheFactory cacheL2Factory;

    private CacheMessagePusher messagePusher;

    public SecondaryCacheManager(SecondaryCacheProperties cacheProperties,
                                 CacheFactory cacheL1Factory,
                                 CacheFactory cacheL2Factory,
                                 CacheMessagePusher messagePusher) {
        this.cacheProperties = cacheProperties;
        this.dynamic = cacheProperties.isDynamic();
        this.cacheL1Factory = cacheL1Factory;
        this.cacheL2Factory = cacheL2Factory;
        this.messagePusher = messagePusher;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            log.debug("Return exist cache ->{}:{}",name,cache);
            return cache;
        }

        if(!dynamic) {
            return null;
        }

        cache = new SecondaryCache(name,cacheL1Factory.newCache(name),cacheL2Factory.newCache(name),cacheProperties,messagePusher);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        log.debug("Create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheMap.keySet();
    }
    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }

        SecondaryCache secondaryCache = (SecondaryCache) cache;
        secondaryCache.clearLocal(key);
    }

    public boolean hasTwoLevel(){
        return cacheL2Factory != null;
    }
}