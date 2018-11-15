package org.echo.share.config;


import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Stream;

/**
 * 缓存配置
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@EnableCaching
public class CacheConfigurations extends CachingConfigurerSupport {

    @Bean
    @Primary
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