package org.echo.spring.cache;

import org.springframework.cache.Cache;

/**
 * 缓存创建工厂
 * @author Liguiqing
 * @since V1.0
 */

public interface CacheFactory {

    Cache newCache(String name);
}