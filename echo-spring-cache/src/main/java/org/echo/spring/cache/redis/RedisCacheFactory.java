package org.echo.spring.cache.redis;

import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import java.time.Duration;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 * @author Liguiqing
 * @since V.0
 */
public class RedisCacheFactory implements CacheFactory {

    private RedisCacheManager redisCacheManager;

    public RedisCacheFactory(RedisConnectionFactory connectionFactory, RedisCacheProperties cacheProperties) {
        Duration ttl = Duration.ofMillis(cacheProperties.getDefaultExpiration());
        RedisCacheConfiguration defaultConfig = redisCacheConfiguration(ttl,cacheProperties);

        Map<String,Long> expires = cacheProperties.getExpires();
        Map<String,RedisCacheConfiguration> rcs = expires.entrySet().stream()
                .collect(Collectors.toMap(
                e -> e.getKey(),
                e -> redisCacheConfiguration(Duration.ofMillis(e.getValue()),cacheProperties)
        ));
        this.redisCacheManager = RedisCacheManager.builder(connectionFactory).cacheDefaults(defaultConfig).withInitialCacheConfigurations(rcs).build();
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
        return redisCacheManager.getCache(name);
    }

}