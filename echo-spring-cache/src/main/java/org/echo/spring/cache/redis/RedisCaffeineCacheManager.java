package org.echo.spring.cache.redis;

import com.github.benmanes.caffeine.cache.Caffeine;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

/**
 * Redis+Caffeine implements of Spring {@see CacheManager}
 *
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
public class RedisCaffeineCacheManager implements CacheManager {

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private RedisCaffeineCacheProperties redisCaffeineCacheProperties;

    private RedisTemplate<Object, Object> redisTemplate;

    private boolean dynamic;

    private Set<String> cacheNames;

    public RedisCaffeineCacheManager(RedisCaffeineCacheProperties redisCaffeineCacheProperties,
                                     RedisTemplate<Object, Object> redisTemplate) {
        super();
        this.redisCaffeineCacheProperties = redisCaffeineCacheProperties;
        this.redisTemplate = redisTemplate;
        this.dynamic = redisCaffeineCacheProperties.isDynamic();
        this.cacheNames = redisCaffeineCacheProperties.getCacheNames();
    }

    @Override
    public Cache getCache(String name) {
        Cache cache = cacheMap.get(name);
        if(cache != null) {
            return cache;
        }

        if(!dynamic && !cacheNames.contains(name)) {
            return null;
        }

        cache = new RedisCaffeineCache(name, redisTemplate, caffeineCache(), redisCaffeineCacheProperties);
        Cache oldCache = cacheMap.putIfAbsent(name, cache);
        log.debug("Create cache instance, the cache name is : {}", name);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheNames;
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(redisCaffeineCacheProperties.getCaffeine().getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(redisCaffeineCacheProperties.getCaffeine().getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }
        if(redisCaffeineCacheProperties.getCaffeine().getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(redisCaffeineCacheProperties.getCaffeine().getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(redisCaffeineCacheProperties.getCaffeine().getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(redisCaffeineCacheProperties.getCaffeine().getInitialCapacity());
        }
        if(redisCaffeineCacheProperties.getCaffeine().getMaximumSize() > 0) {
            cacheBuilder.maximumSize(redisCaffeineCacheProperties.getCaffeine().getMaximumSize());
        }
        if(redisCaffeineCacheProperties.getCaffeine().getRefreshAfterWrite() > 0) {
            cacheBuilder.refreshAfterWrite(redisCaffeineCacheProperties.getCaffeine().getRefreshAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        return cacheBuilder.build();
    }

    public void clearLocal(String cacheName, Object key) {
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }

        RedisCaffeineCache redisCaffeineCache = (RedisCaffeineCache) cache;
        redisCaffeineCache.clearLocal(key);
    }
}