package org.echo.spring.cache.redis;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author Liguiqing
 * @since V.0
 */

public class RedisCacheFactory implements CacheFactory {
    private RedisCacheManager redisCacheManager;

    public RedisCacheFactory(RedisConnectionFactory connectionFactory) {
        //this.cacheProperties = cacheProperties;
        //this.redisTemplate = redisTemplate;
        this.redisCacheManager = RedisCacheManager.create(connectionFactory);
    }

    @Override
    public Cache newCache(String name) {
        return this.redisCacheManager.getCache(name);
//        Cache cache =  redisCacheManager.getCache(name);
//        if(cache == null){
//            cache = redisCacheManager.
//        }
        //return new org.springframework.data.redis.cache.RedisCache(name,redisTemplate,cacheProperties);
    }
}