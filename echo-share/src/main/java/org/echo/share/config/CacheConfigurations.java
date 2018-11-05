package org.echo.share.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.lang.Nullable;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfigurations extends CachingConfigurerSupport {

    public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Primary
    @Bean("noneCacheManager")
    public CacheManager defaultCacheManager(){
        return new CacheManager(){

            @Nullable
            @Override
            public Cache getCache(String s) {
                return null;
            }

            @Override
            public Collection<String> getCacheNames() {
                return null;
            }
        };
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(@Value("${spring.redis.sentinel.master}")String master,
                                                         @Value("${spring.redis.sentinel.nodes}") String nodes){
        try {
            RedisSentinelConfiguration client = new RedisSentinelConfiguration(master,Stream.of(nodes.split(";")).collect(Collectors.toSet()));
            JedisConnectionFactory redisConnectionFactory = new JedisConnectionFactory(client);
            redisConnectionFactory.getConnection();
            return redisConnectionFactory;
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
        return null;
    }


    @Bean("ehCacheManager")
    public CacheManager ehCacheCacheManager() {
        EhCacheManagerFactoryBean cmfb = new EhCacheManagerFactoryBean();
        cmfb.setConfigLocation(new ClassPathResource("ehcache.xml"));
        cmfb.setShared(true);
        return new EhCacheCacheManager(cmfb.getObject());
    }

    @Bean("redisCacheManager")
    public CacheManager redisCacheManager(RedisConnectionFactory cf) {
        if(cf == null)
            return null;
        try {
            RedisCacheWriter cacheWriter  = RedisCacheWriter.nonLockingRedisCacheWriter(cf);
            RedisCacheManager cacheManager = new RedisCacheManager(cacheWriter,RedisCacheConfiguration.defaultCacheConfig(),true);
            return cacheManager;
        }catch (Exception e){
            log.error(e.getLocalizedMessage());
        }
        return null;
    }

    @Bean
    public CacheManager cacheManager(List<CacheManager> managers) {
        log.debug("Create Cache ");
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        cacheManager.setCacheManagers(managers);
        cacheManager.setFallbackToNoOpCache(true);
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