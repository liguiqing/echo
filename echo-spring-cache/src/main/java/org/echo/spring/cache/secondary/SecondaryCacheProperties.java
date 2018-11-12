package org.echo.spring.cache.secondary;

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
public class SecondaryCacheProperties {

    /** 是否存储空值，默认false，防止缓存穿透*/
    private boolean cacheNullValues = false;

    /** 是否动态根据cacheName创建Cache的实现，默认true*/
    private boolean dynamic = true;

    /** 缓存key的前缀*/
    private String cachePrefix = "echo:";

    /**是否启动二级缓存,默认值不启用.启用时必须配置redis服务**/
    private boolean level2Enabled = false;

    /** 缓存消息发布/订阅主题 **/
    private String cacheMessageTopic = "echo:cache";

}