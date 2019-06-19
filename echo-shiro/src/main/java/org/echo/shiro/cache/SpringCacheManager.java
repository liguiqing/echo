package org.echo.shiro.cache;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class SpringCacheManager extends AbstractCacheManager {

    public final static int DEFAULT_CACHE_LEVEL = 2;

    private org.springframework.cache.CacheManager cacheManager;

    private int cacheLevel;

    @Override
    protected Cache createCache(String name) {
        org.springframework.cache.Cache springCache =  cacheManager.getCache(springCacheName(name));
        return new SpringCache(springCache);
    }

    private String springCacheName(String cacheName){
        return cacheName.concat("#-1#-1#").concat(Integer.toString(cacheLevel));
    }
}