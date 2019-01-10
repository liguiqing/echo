package org.echo.spring.cache;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.caffeine.CaffeineCaches;
import org.echo.spring.cache.secondary.SecondaryCache;
import org.redisson.spring.cache.RedissonCache;
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

    public static int size(Cache cache) {

        Cache nativeCache = toNativeCache(cache);
        if (Objects.isNull(cache))
            return 0;

        if(nativeCache instanceof CaffeineCache){
            return CaffeineCaches.size((CaffeineCache)nativeCache);
        }

        if(nativeCache instanceof RedissonCache){
            RedissonCache redissonCache = (RedissonCache) nativeCache;
            return redissonCache.getNativeCache().size();
        }

        return 0;
    }

    public static Set keys (Cache cache){
        Cache nativeCache = toNativeCache(cache);
        if(nativeCache instanceof CaffeineCache){
            return CaffeineCaches.keys((CaffeineCache)nativeCache);
        }

        if(nativeCache instanceof RedissonCache){
            RedissonCache redissonCache = (RedissonCache) nativeCache;
            return redissonCache.getNativeCache().keySet();
        }
        return Collections.emptySet();
    }

    public static Collection values(Cache cache){
        Cache nativeCache = toNativeCache(cache);
        if(nativeCache instanceof CaffeineCache){
            return CaffeineCaches.values((CaffeineCache)nativeCache);
        }

        if(nativeCache instanceof RedissonCache){
            RedissonCache redissonCache = (RedissonCache) nativeCache;
            return redissonCache.getNativeCache().values();
        }

        return Collections.emptySet();
    }

    private static Cache toNativeCache(Cache cache){
        if(cache == null)
            return null;
        if(cache instanceof SecondaryCache)
            return (Cache)cache.getNativeCache();
        return cache;
    }
}