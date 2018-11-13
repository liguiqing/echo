package org.echo.spring.cache.caffeine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheProperties;
import org.echo.util.CollectionsUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * CaffeineProperties Properties
 *
 * @author Liguiqing
 * @since V1.0
 */

@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "spring.cache.secondary.caffeine")
public class CaffeineCacheProperties implements CacheProperties {

    private CaffeineProperties defaultProp;

    private List<CaffeineProperties> cachesOnBoot;

    @Override
    public String getName() {
        return defaultProp.getName();
    }

    protected CaffeineProperties getProp(String name){
        if(CollectionsUtil.isNotNullAndNotEmpty(this.cachesOnBoot)){
            for(CaffeineProperties p:cachesOnBoot){
                if(p.getName().equalsIgnoreCase(name))
                    return p;
            }
        }

        return defaultProp;
    }
}