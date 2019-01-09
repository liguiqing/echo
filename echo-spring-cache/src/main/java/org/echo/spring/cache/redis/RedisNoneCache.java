package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisNoneCache extends AbstractValueAdaptingCache {

    public RedisNoneCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    protected Object lookup(Object key) {
        warn();
        return null;
    }

    @Override
    public String getName() {
        warn();
        return null;
    }

    @Override
    public Object getNativeCache() {
        return getName();
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        warn();
        return null;
    }

    @Override
    public void put(Object key, Object value) {
        warn();
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        warn();
        return null;
    }

    @Override
    public void evict(Object key) {
        warn();
    }

    @Override
    public void clear() {
        warn();
    }

    private void  warn(){
        log.warn("Redis has some problem,please check the server can be access!");
    }
}