package org.echo.share.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.lang.reflect.Method;
import java.util.stream.Stream;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@EnableCaching
@PropertySource("classpath:/redis.properties")
public class CacheConfigurations extends CachingConfigurerSupport {

    @Bean("redisPlaceholder")
    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(PropertySourcesPlaceholderConfigurer propertySource){

        RedisSentinelConfiguration client = new RedisSentinelConfiguration();
        JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(client);
        return redisConnectionFactory;
    }


    @Bean
    public CacheManager cacheManager(RedisConnectionFactory cf) {
        RedisCacheWriter cacheWriter  = RedisCacheWriter.nonLockingRedisCacheWriter(cf);
        RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter,RedisCacheConfiguration.defaultCacheConfig(),true);
        return cacheManager;
    }

    @Bean
    public KeyGenerator keyGenerator() {
        return (Object o, Method method, Object... objects)->{
            StringBuilder sb = new StringBuilder();
            sb.append(o.getClass().getName());
            sb.append(method.getName());
            Stream.of(objects).forEach(oo->sb.append(oo.toString()));
            return sb.toString();
        };
    }
}