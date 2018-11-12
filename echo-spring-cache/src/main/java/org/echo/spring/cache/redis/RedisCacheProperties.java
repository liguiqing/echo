package org.echo.spring.cache.redis;

import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Liguiqing
 * @since V1.0
 */

@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.cache.secondary.redis")
public class RedisCacheProperties implements CacheProperties {

    private String name;

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 全局过期时间，单位毫秒，默认0,永不过期*/
    private long defaultExpiration = 0;

    /** cacheName的过期时间，单位毫秒，优先级比defaultExpiration高*/
    private Map<String, Long> expires = Maps.newHashMap();

    /** 缓存key的前缀*/
    private String cachePrefix = "echo:";

    private Set<String> hostsAndPorts = new HashSet<>();

    private Standalone standalone = new Standalone();
}