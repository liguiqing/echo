package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author Liguiqing
 * @since V.0
 */
@Slf4j
public class RedisCacheFactory implements CacheFactory {

    private RedisCacheProperties cacheProperties;

    private JedisPool jedisPool;

    private RedisTemplate<Object,Object> redisTemplate;

    public RedisCacheFactory(RedisCacheProperties cacheProperties, JedisPool jedisPool, RedisTemplate<Object, Object> redisTemplate) {
        this.cacheProperties = cacheProperties;
        this.jedisPool = jedisPool;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Cache newCache(String name) {
        return this.newCache(name, -1, -1);
    }

    @Override
    public Cache newCache(String name, long ttl, long maxIdleSecond) {
        if(isRedisConnected()){
            return new XRedisCache(name, this.cacheProperties.getCachePrefix(), cacheProperties.isCacheNullValues(),
                    cacheProperties.getTtl(name,ttl), this.redisTemplate, this.jedisPool);
        }
        return new RedisNoneCache(false);
    }

    private boolean isRedisConnected(){
        return jedisPool.getResource().isConnected();
    }
}