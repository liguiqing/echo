package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.CacheFactory;
import org.redisson.api.RMap;
import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.redisson.spring.cache.CacheConfig;
import org.redisson.spring.cache.RedissonCache;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Liguiqing
 * @since V.0
 */
@Slf4j
public class RedisCacheFactory implements CacheFactory {

    private RedissonClient redissonClient;

    private RedisCacheManager redisCacheManager;

    private RedisCacheProperties cacheProperties;

    public RedisCacheFactory(RedisConnectionFactory connectionFactory, RedisCacheProperties cacheProperties) {
        Duration ttl = Duration.ofMillis(cacheProperties.getDefaultExpiration());
        RedisCacheConfiguration defaultConfig = redisCacheConfiguration(ttl,cacheProperties);

        Map<String,Long> expires = cacheProperties.getExpires();
        Map<String,RedisCacheConfiguration> rcs = expires.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                e -> redisCacheConfiguration(Duration.ofMillis(e.getValue()),cacheProperties)
        ));
        this.redisCacheManager = RedisCacheManager.builder(connectionFactory).cacheDefaults(defaultConfig).withInitialCacheConfigurations(rcs).build();
        this.cacheProperties = cacheProperties;
    }

    public RedisCacheFactory(RedissonClient redissonClient, RedisCacheProperties cacheProperties){
        this.redissonClient = redissonClient;
        this.cacheProperties = cacheProperties;
    }

    private RedisCacheConfiguration redisCacheConfiguration(Duration ttl,RedisCacheProperties cacheProperties){
        String keyPrefix = cacheProperties.getCachePrefix();
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig().entryTtl(ttl).prefixKeysWith(keyPrefix);
        if(!cacheProperties.isCacheNullValues()){
            return configuration.disableCachingNullValues();
        }else{
            return configuration;
        }
    }

    @Override
    public Cache newCache(String name) {
        try {
            if (redissonClient != null) {
                RMap<Object, Object> map = redissonClient.getMapCache(getRedissonCacheName(name), new FstCodec());
                return new RedissonCache(map, cacheProperties.isCacheNullValues());
            }
            return redisCacheManager.getCache(name);
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
            return new RedisNoneCache(false);
        }
    }

    @Override
    public Cache newCache(String name, long ttl, long maxIdleSecond) {
        if(redissonClient != null){
            if(ttl < 0)
                ttl = 0;

            if(maxIdleSecond < 0)
                maxIdleSecond = 0;

            CacheConfig config = new CacheConfig(ttl * 1000,maxIdleSecond * 1000);
            RMapCache<Object,Object> map = redissonClient.getMapCache(getRedissonCacheName(name),new FstCodec());
            return new RedissonCache(map, config, cacheProperties.isCacheNullValues());
        }
        return newCache(name);
    }


    private String getRedissonCacheName(String name){
        return (StringUtils.isEmpty(cacheProperties.getCachePrefix())?"echo":cacheProperties.getCachePrefix()).concat(":").concat(name).concat(":");
    }
}