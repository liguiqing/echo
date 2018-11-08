package org.echo.spring.cache.caffeine;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;

/**
 * @author Liguiqing
 * @since V3.0
 */

public class CaffeineCacheFactory implements CacheFactory {

    private CaffeineCacheProperties caffeineCacheProperties;

    public CaffeineCacheFactory(CaffeineCacheProperties caffeineCacheProperties) {
        this.caffeineCacheProperties = caffeineCacheProperties;
    }

    @Override
    public Cache newCache(String name) {
        return new CaffeineCache(name,this.caffeineCacheProperties);
    }
}