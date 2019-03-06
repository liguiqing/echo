package org.echo.spring.cache.config;


import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.cache.support.CompositeCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * 缓存配置
 *
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@EnableCaching
public class CacheConfigurations extends CachingConfigurerSupport {

    private static Object generate(Object o, Method method, Object... objects) {
        StringBuilder sb = new StringBuilder();
        sb.append(o.getClass().getName());
        sb.append(method.getName());
        Stream.of(objects).forEach(oo -> sb.append(oo.toString()));
        return sb.toString();
    }

    @Bean
    @Primary
    public CacheManager cacheManager(Optional<List<CacheManager>> managers) {
        CompositeCacheManager cacheManager = new CompositeCacheManager();
        managers.ifPresent(cacheManager::setCacheManagers);
        cacheManager.setFallbackToNoOpCache(true);
        return cacheManager;
    }

    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return CacheConfigurations::generate;
    }
}