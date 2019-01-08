package org.echo.shiro.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@Component
@ConfigurationProperties(prefix = "shiro")
public class ShiroProperties {

    private long maxIdleSecond = 12000;

    private Map<String,CacheProperties> cachePropertiesMap;

    public String getCacheName(String cacheName){
        if(cachePropertiesMap == null){
            return defaultCacheName(cacheName);
        }
        CacheProperties cacheProperties = cachePropertiesMap.get(cacheName);
        if(cacheProperties != null){
            return defaultCacheName(cacheName);
        }
        return springCacheName(cacheName, cacheProperties.getMaxIdleSecond());
    }

    private String defaultCacheName(String cacheName){
        return springCacheName(cacheName,this.maxIdleSecond);
    }

    private String springCacheName(String cacheName,long maxIdleSecond){
        return cacheName.concat("#"+maxIdleSecond).concat("#"+maxIdleSecond+"#2");
    }

}