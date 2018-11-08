package org.echo.spring.cache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class SecondryrCacheManager implements CacheManager {

    private SecondaryCacheProperties cacheProperties;

    private boolean dynamic;

    private Set<String> cacheNames;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheFactory cacheL1Factory;

    private CacheFactory cacheL2Factory;

    private CacheMessagePusher messagePusher;

    public SecondryrCacheManager(SecondaryCacheProperties cacheProperties,
                                 CacheFactory cacheL1Factory,
                                 CacheFactory cacheL2Factory,
                                 CacheMessagePusher messagePusher) {
        //this.cacheProperties = cacheProperties;
        this.dynamic =false;
        this.cacheNames = cacheNames;
        this.cacheMap = cacheMap;
        this.cacheL1Factory = cacheL1Factory;
        this.cacheL2Factory = cacheL2Factory;
        this.messagePusher = messagePusher;
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            log.debug("Return exist cache ->{}:{}",name,cache);
            return cache;
        }

        if(!dynamic && !cacheNames.contains(name)) {
            return null;
        }
        cache = new SecondaryCache(name,cacheL1Factory.newCache(name),cacheL2Factory.newCache(name),cacheProperties,messagePusher);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        log.debug("Create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }
}