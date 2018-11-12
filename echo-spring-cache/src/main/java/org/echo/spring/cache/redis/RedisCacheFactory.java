package org.echo.spring.cache.redis;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.util.List;

/**
 *
 * @author Liguiqing
 * @since V.0
 */
public class RedisCacheFactory implements CacheFactory<RedisCacheProperties> {
    private RedisCacheManager redisCacheManager;

    public RedisCacheFactory(RedisConnectionFactory connectionFactory) {
        this.redisCacheManager = RedisCacheManager.create(connectionFactory);
    }

    @Override
    public Cache newCache(String name) {
        return this.redisCacheManager.getCache(name);
    }

    @Override
    public Cache newCache(RedisCacheProperties redisCacheProperties) {
        return this.newCache(redisCacheProperties.getName());
    }

    @Override
    public List<Cache> creates() {

        return null;
    }
}