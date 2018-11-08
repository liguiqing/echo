package org.echo.spring.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.AbstractMultiAdaptingCache;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class CaffeineCache extends AbstractMultiAdaptingCache {

    private String name;

    private CaffeineCacheProperties caffeineCacheProperties;

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache;

    public CaffeineCache(String name,CaffeineCacheProperties caffeineCacheProperties) {
        super(caffeineCacheProperties.isCacheNullValues());
        this.name = name;
        this.caffeineCacheProperties = caffeineCacheProperties;
        this.caffeineCache = caffeineCache();
    }

    public boolean allowNullValues(){
        return this.caffeineCacheProperties.isCacheNullValues();
    }

    @Override
    public Object lookup(Object key) {
        Object value = caffeineCache.getIfPresent(key);
        if(value != null) {
            log.debug("Get cache from caffeine of key -> {}", key);
            return value;
        }
        log.debug("There is no cache in caffeine of key -> {}", key);
        return null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this.caffeineCache;
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("Get cache from caffeine of key {}",key);

        return super.get(key, valueLoader);
    }

    protected void doPut(Object key, Object value) {
        log.debug("Put to caffeine by key {}->{}",key,value);

        caffeineCache.put(key, value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put to caffeine if absent of {}->{}",key,value);

        caffeineCache.get(key, k -> value);
        return toValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict from caffeine by {}",key);

        caffeineCache.invalidate(key);
    }

    @Override
    public void clear() {
        log.debug("Clear from caffeine ");

        caffeineCache.cleanUp();
    }
    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(caffeineCacheProperties.getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(caffeineCacheProperties.getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }
        if(caffeineCacheProperties.getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(caffeineCacheProperties.getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(caffeineCacheProperties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(caffeineCacheProperties.getInitialCapacity());
        }
        if(caffeineCacheProperties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(caffeineCacheProperties.getMaximumSize());
        }
        if(caffeineCacheProperties.getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(caffeineCacheProperties.getRefreshAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        return cacheBuilder.build();
    }
    
}