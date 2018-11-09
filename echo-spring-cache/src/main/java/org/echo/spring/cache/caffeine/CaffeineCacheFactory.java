package org.echo.spring.cache.caffeine;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.echo.spring.cache.CacheFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;

import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V3.0
 */

public class CaffeineCacheFactory implements CacheFactory {

    private CaffeineCacheProperties caffeineCacheProperties;

    public CaffeineCacheFactory(CaffeineCacheProperties caffeineCacheProperties) {
        this.caffeineCacheProperties = caffeineCacheProperties;
    }

    @Override
    public Cache newCache(String name) {
        return new CaffeineCache(name,caffeineCache());
    }

    private com.github.benmanes.caffeine.cache.Cache<Object, Object> caffeineCache(){
        Caffeine<Object, Object> cacheBuilder = Caffeine.newBuilder();
        if(caffeineCacheProperties.getExpireAfterAccess() > 0) {
            cacheBuilder.expireAfterAccess(caffeineCacheProperties.getExpireAfterAccess(),
                    TimeUnit.MILLISECONDS);
        }
        if(caffeineCacheProperties.getExpireAfterWrite() > 0) {
            cacheBuilder.expireAfterWrite(caffeineCacheProperties.getExpireAfterWrite(),
                    TimeUnit.MILLISECONDS);
        }
        if(caffeineCacheProperties.getInitialCapacity() > 0) {
            cacheBuilder.initialCapacity(caffeineCacheProperties.getInitialCapacity());
        }
        if(caffeineCacheProperties.getMaximumSize() > 0) {
            cacheBuilder.maximumSize(caffeineCacheProperties.getMaximumSize());
        }
        //TODO 这个参数设置有问题,记得处理
//        if(caffeineCacheProperties.getRefreshAfterWrite() > 0) {
//            cacheBuilder.refreshAfterWrite(caffeineCacheProperties.getRefreshAfterWrite(),
//                    TimeUnit.MILLISECONDS);
//        }

        return cacheBuilder.build();
    }
}