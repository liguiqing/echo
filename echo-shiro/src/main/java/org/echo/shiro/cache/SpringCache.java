package org.echo.shiro.cache;

import lombok.AllArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.echo.spring.cache.NativeCaches;

import java.util.Collection;
import java.util.Set;

/**
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
public class SpringCache<K, V> implements Cache<K, V> {

    private org.springframework.cache.Cache springCache;

    @Override
    public V get(K k) throws CacheException {
        return (V)springCache.get(k).get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        return (V)this.springCache.putIfAbsent(k,v).get();
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = get(k);
        this.springCache.evict(k);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        this.springCache.clear();
    }

    @Override
    public int size() {
        return NativeCaches.size(this.springCache);
    }

    @Override
    public Set<K> keys() {
        return NativeCaches.keys(this.springCache);
    }

    @Override
    public Collection<V> values() {
        return NativeCaches.values(this.springCache);
    }
}