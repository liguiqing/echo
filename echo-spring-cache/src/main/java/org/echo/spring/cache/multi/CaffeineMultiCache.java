package org.echo.spring.cache.multi;

import org.echo.spring.cache.caffeine.CaffeineCache;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * @author Liguiqing
 * @since V3.0
 */

public class CaffeineMultiCache extends AbstractValueAdaptingCache implements CacheIterator {

    private CaffeineCache caffeineCache;

    private CacheIterator next;

    public CaffeineMultiCache(CaffeineCache caffeineCache) {
        super(caffeineCache.allowNullValues());
        this.caffeineCache = caffeineCache;
    }

    public CaffeineMultiCache(CaffeineCache caffeineCache, CacheIterator next) {
        super(caffeineCache.allowNullValues());
        this.caffeineCache = caffeineCache;
        this.next = next;
    }

    @Override
    public boolean hasNext() {
        return this.next != null;
    }

    @Override
    public CacheIterator next() {
        return this.next;
    }

    @Override
    public String getName() {
        return caffeineCache.getName();
    }

    @Override
    public Object getNativeCache() {
        return caffeineCache.getNativeCache();
    }

    @Override
    public ValueWrapper get(Object key) {
        return caffeineCache.get(key);
    }

    @Override
    protected Object lookup(Object key) {
        return caffeineCache.lookup(key);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        return caffeineCache.get(key,valueLoader);
    }

    @Override
    public void put(Object key, Object value) {
        caffeineCache.put(key,value);
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return caffeineCache.putIfAbsent(key,value);
    }

    @Override
    public void evict(Object key) {
        caffeineCache.evict(key);
    }

    @Override
    public void clear() {
        caffeineCache.clear();
    }
}