package org.echo.spring.cache.redis;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Liguiqing
 * @since V.0
 */

public class RedisCacheFactory implements CacheFactory {

    private RedisCacheProperties cacheProperties;

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheFactory(RedisCacheProperties cacheProperties,RedisTemplate<Object, Object> redisTemplate) {
        this.cacheProperties = cacheProperties;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Cache newCache(String name) {
        return new RedisCache(name,redisTemplate,cacheProperties);
    }
}