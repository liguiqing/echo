package org.echo.spring.cache;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.springframework.cache.support.AbstractValueAdaptingCache;

import java.util.concurrent.Callable;

/**
 * 多级缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public abstract class AbstractMultiAdaptingCache extends AbstractValueAdaptingCache {


    /**
     * Create an {@code AbstractValueAdaptingCache} with the given setting.
     *
     * @param allowNullValues whether to allow for {@code null} values
     */
    protected AbstractMultiAdaptingCache(boolean allowNullValues) {
        super(allowNullValues);
    }

    @Override
    public <T> T get(Object key, Callable<T> valueLoader) {
        T value = (T)lookup(key);
        if(value != null) {
            return value;
        }

        try {
            value = valueLoader.call();
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        }

        if(value == null)
            return null;

        Object storeValue = toStoreValue(value);
        put(key, storeValue);

        return value;
    }


    @Override
    public void put(Object key, Object value) {
        if (value == null && !super.isAllowNullValues()) {
            this.evict(key);
            return;
        }

        doPut(key, value);
    }

    protected abstract void doPut(Object key, Object value);
}