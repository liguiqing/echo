package org.echo.spring.cache.multi;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.cache.secondary")
public class MultiCacheProperties {

    private Set<String> cacheNames = Sets.newHashSet();

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 是否动态根据cacheName创建Cache的实现，默认true*/
    private boolean dynamic = true;

    /** 缓存key的前缀*/
    private String cachePrefix = "echo:";

    /**是否启动二级缓存,默认值不启用.启用时必须配置redis服务**/
    private boolean level2Enabled = false;

    /**是否启动二级缓存,默认值不启用.启用时必须配置redis服务**/
    private String level2Topic = "echo:cache";
}