package org.echo.spring.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.echo.spring.cache.CacheFactory;
import org.echo.util.CollectionsUtil;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 *  Caffeine cache Factory
 *
 * @author Liguiqing
 * @since V1.0
 */

public class CaffeineCacheFactory implements CacheFactory<CaffeineCacheProperties> {

    private CaffeineCacheProperties caffeineCacheProperties;

    public CaffeineCacheFactory(CaffeineCacheProperties caffeineCacheProperties) {
        this.caffeineCacheProperties = caffeineCacheProperties;
    }

    @Override
    public Cache newCache(String name) {
        return new CaffeineCache(name,caffeineCache(this.caffeineCacheProperties.getDefaultProp()));
    }

    @Override
    public Cache newCache(CaffeineCacheProperties caffeineCacheProperties) {
        CaffeineProperties prop = caffeineCacheProperties.getDefaultProp();
        return new CaffeineCache(prop.getName(),caffeineCache(prop));
    }

    @Override
    public List<Cache> creates() {
        if(CollectionsUtil.isNotNullAndNotEmpty(this.caffeineCacheProperties.getCachesOnBoot())){
            List<CaffeineProperties> props = this.caffeineCacheProperties.getCachesOnBoot();
            return props.stream().map(prop -> new CaffeineCache(prop.getName(),caffeineCache(prop))).collect(Collectors.toList());
        }
        return null;
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(CaffeineProperties properties){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(properties.getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(properties.getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }
        if(properties.getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(properties.getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(properties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(properties.getInitialCapacity());
        }
        if(properties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(properties.getMaximumSize());
        }
        //TODO 这个参数设置有问题,记得处理
//        if(caffeineCacheProperties.getRefreshAfterWrite() > 0) {
//            cacheBuilder.refreshAfterWrite(caffeineCacheProperties.getRefreshAfterWrite(),
//                    TimeUnit.MILLISECONDS);
//        }

        return cacheBuilder.build();
    }
}