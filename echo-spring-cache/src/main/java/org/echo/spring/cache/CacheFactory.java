package org.echo.spring.cache;

import org.springframework.cache.Cache;

import java.util.List;

/**
 * 缓存创建工厂
 * @author Liguiqing
 * @since V1.0
 */

public interface CacheFactory<P extends CacheProperties> {

    Cache newCache(String name);

    Cache newCache(P p);

    List<Cache> creates();
}