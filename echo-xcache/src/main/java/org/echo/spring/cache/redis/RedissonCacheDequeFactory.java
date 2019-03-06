package org.echo.spring.cache.redis;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheDequeFactory;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;

import java.util.Deque;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class RedissonCacheDequeFactory implements CacheDequeFactory {

    private RedissonClient redissonClient;

    private RedisCacheProperties cacheProperties;

    @Override
    public Deque getDeque(String cacheName) {
        return redissonClient.getBlockingDeque(cacheProperties.getCacheName(cacheName),new FstCodec());
    }
}