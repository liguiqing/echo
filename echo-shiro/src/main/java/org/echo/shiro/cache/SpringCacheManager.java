package org.echo.shiro.cache;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.cache.AbstractCacheManager;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.echo.shiro.config.ShiroProperties;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class SpringCacheManager extends AbstractCacheManager {

    private org.springframework.cache.CacheManager cacheManager;

    private ShiroProperties properties;

    @Override
    protected Cache createCache(String name) throws CacheException {
        String springCacheName = properties.getCacheName(name);
        org.springframework.cache.Cache springCache =  cacheManager.getCache(springCacheName);
        return new SpringCache(springCache);
    }
}