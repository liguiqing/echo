package org.echo.shiro.cache;

import lombok.AllArgsConstructor;
import org.apache.shiro.cache.Cache;
import org.echo.xcache.NativeCaches;

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
    public V get(K k) {
        var o = proxyCache.get(k);
        return o == null?null:(V)o.get();
    }

    @Override
    public V put(K k, V v)  {
        this.proxyCache.put(k,v);
        return v;
    }

    @Override
    public V remove(K k){
        V v = get(k);
        this.proxyCache.evict(k);
        return v;
    }

    @Override
    public void clear(){
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