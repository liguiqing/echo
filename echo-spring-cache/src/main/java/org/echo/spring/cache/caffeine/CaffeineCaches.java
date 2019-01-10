package org.echo.spring.cache.caffeine;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V2.0
 */

public class CaffeineCaches {

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
}