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

    private org.springframework.cache.Cache proxyCache;

    @Override
    public V get(K k) throws CacheException {
        if(proxyCache.get(k) == null)
            return null;
        return (V) proxyCache.get(k).get();
    }

    @Override
    public V put(K k, V v) throws CacheException {
        return (V)this.proxyCache.putIfAbsent(k,v).get();
    }

    @Override
    public V remove(K k) throws CacheException {
        V v = get(k);
        this.proxyCache.evict(k);
        return v;
    }

    @Override
    public void clear() throws CacheException {
        this.proxyCache.clear();
    }

    @Override
    public int size() {
        return NativeCaches.size(this.proxyCache);
    }

    @Override
    public Set<K> keys() {
        return NativeCaches.keys(this.proxyCache);
    }

    @Override
    public Collection<V> values() {
        return NativeCaches.values(this.proxyCache);
    }
}