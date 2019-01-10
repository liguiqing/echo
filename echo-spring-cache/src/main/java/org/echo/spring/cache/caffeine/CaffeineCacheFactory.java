package org.echo.spring.cache.caffeine;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

/**
 *  Caffeine cache Factory
 *
 * @author Liguiqing
 * @since V1.0
 */

public class CaffeineCacheFactory implements CacheFactory {

    private CaffeineCacheProperties caffeineCacheProperties;

    public CaffeineCacheFactory(CaffeineCacheProperties caffeineCacheProperties) {
        this.caffeineCacheProperties = caffeineCacheProperties;
    }

    @Override
    public Cache newCache(String name) {
        return new CaffeineCache(name,CaffeineCaches.newCache(this.caffeineCacheProperties.getProp(name)));
    }

    @Override
    public Cache newCache(String name, long ttl, long maxIdleSecond) {
        CaffeineProperties cp = new CaffeineProperties();
        cp.setExpireAfterWrite(ttl * 1000)
                .setExpireAfterAccess(maxIdleSecond * 1000);
        return new CaffeineCache(name,CaffeineCaches.newCache(cp));
    }
}