package org.echo.spring.cache;

import org.springframework.cache.Cache;

/**
 * 缓存创建工厂
 * @author Liguiqing
 * @since V1.0
 */

public interface CacheFactory {

    /**
     * Create {@link Cache} with name
     * @param name cache name
     * @return {@link Cache}
     */
    Cache newCache(String name);

    /**
     * Create {@link Cache} with name and ttl and maxIdleSecond
     * @param name cache name
     * @param ttl Second of time to live
     * @param maxIdleSecond Second of maxIdle
     * @return {@link Cache}
     */
    Cache newCache(String name,long ttl,long maxIdleSecond);
}