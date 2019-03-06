package org.echo.spring.cache;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.caffeine.CaffeineCaches;
import org.echo.spring.cache.redis.XRedisCache;
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
    private NativeCaches() {
        throw new AssertionError("No org.echo.spring.cache.NativeCaches instances for you!");
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
        if(cache instanceof SecondaryCache)
            return (Cache)cache.getNativeCache();
        return cache;
    }
}