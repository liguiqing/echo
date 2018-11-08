package org.echo.spring.cache.multi;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.AbstractMultiAdaptingCache;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.springframework.cache.Cache;

/**
 * Spring 多级缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class MultiCache extends AbstractMultiAdaptingCache {
    private String name;

    private String l2Topic;

    private CacheMessagePusher messagePusher;

    private MultiCacheProperties cacheProperties;

    private CacheIterator cacheIterator;

    private Cache cache;

    public MultiCache(Cache cache,MultiCacheProperties cacheProperties, CacheMessagePusher messagePusher) {
        super(cacheProperties.isCacheNullValues());
        this.cache = cache;
        if(this.cache instanceof  CacheIterator){
            this.cacheIterator = (CacheIterator)cache;
        }
        this.cacheProperties = cacheProperties;
        this.messagePusher = messagePusher;
    }

    private boolean isMulti(){
        return this.cacheIterator != null;
    }

    @Override
    protected void doPut(Object key, Object value) {
        if(isMulti())
            putToCache(key,value);
        else
            cache.put(key,value);
    }

    private void putToCache(Object key, Object value){
        cacheIterator.put(key,value);
        if(cacheIterator.hasNext()){
            cacheIterator.next().put(key,value);
        }
    }

    @Override
    protected Object lookup(Object key) {
        log.debug("Lookup Cache of key {}",key);

        if(isMulti()){
            return cacheIterator.get(key);
        }

        return cache.get(key);
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Object getNativeCache() {
        return this;
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        return null;
    }

    @Override
    public void evict(Object key) {

    }

    @Override
    public void clear() {

    }
}