package org.echo.spring.cache.redis;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * Redis+Caffeine
 * 配置类,从spring boot配置文件中请取redis相关配置
 *
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.cache.redis-caffeine")
public class RedisCaffeineCacheProperties {

    private Set<String> cacheNames = Sets.newHashSet();

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 是否动态根据cacheName创建Cache的实现，默认true*/
    private boolean dynamic = true;

    /** 缓存key的前缀*/
    private String cachePrefix;

    private Redis redis = new Redis();

    private Caffeine caffeine = new Caffeine();
}