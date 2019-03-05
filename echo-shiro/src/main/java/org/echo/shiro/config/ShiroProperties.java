package org.echo.shiro.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;

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

    private int cached = 1;

    private Map<String,Long> cachePropertiesMap;

    public String getCacheName(String cacheName){
        if(this.cachePropertiesMap == null){
            return defaultCacheName(cacheName);
        }
        Long maxIdle = this.getCachePropertiesMap().get(cacheName);
        if(Objects.isNull(maxIdle)){
            return defaultCacheName(cacheName);
        }
        return springCacheName(cacheName, maxIdle);
    }

    private String defaultCacheName(String cacheName){
        return springCacheName(cacheName,this.maxIdleSecond);
    }

    private String springCacheName(String cacheName,long maxIdleSecond){
        return cacheName.concat("#"+maxIdleSecond).concat("#"+maxIdleSecond+"#").concat(Integer.toString(cached));
    }

}