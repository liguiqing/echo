package org.echo.spring.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class CaffeineCaches {

    private CaffeineCaches() {
        throw new AssertionError("No org.echo.spring.cache.caffeine.CaffeineCaches instances for you!");
    }

    public static Cache newCache(CaffeineProperties properties) {
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(properties.getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(properties.getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }

        if(properties.getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(properties.getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(properties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(properties.getInitialCapacity());
        }
        if(properties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(properties.getMaximumSize());
        }
        return cacheBuilder.build();
    }

    public static int size(CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return Long.valueOf(nativeCache.estimatedSize()).intValue();
    }

    public static Set keys (CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return nativeCache.asMap().keySet();
    }

    public static Collection values (CaffeineCache cache){
        Cache nativeCache = cache.getNativeCache();
        return nativeCache.asMap().values();
    }
}