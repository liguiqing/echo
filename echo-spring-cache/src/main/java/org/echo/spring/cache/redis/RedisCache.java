package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.spring.cache.CustomizedCache;
import org.echo.util.Threads;
import org.springframework.cache.support.AbstractValueAdaptingCache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 基于Redis 的cache
 * {@see org.springframework.data.redis.cache.RedisCache} 不好控制
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisCache extends AbstractValueAdaptingCache implements CustomizedCache {

    private String name;

    private RedisCacheProperties cacheProperties;

    private RedisTemplate<Object, Object> redisTemplate;

    private String cachePrefix;

    protected RedisCache(String name, RedisCacheProperties cacheProperties,
                         RedisTemplate<Object, Object> redisTemplate) {
        super(cacheProperties.isCacheNullValues());
        this.name = name;
        this.cacheProperties = cacheProperties;
        this.cachePrefix = cacheProperties.getCachePrefix();
        this.redisTemplate = redisTemplate;
    }

    @Override
    @Nullable
    protected Object lookup(Object key) {
        log.debug("Lookup Object from redis by key [{}]",key);
        Object cacheKey = getKey(key);
        return redisTemplate.opsForValue().get(cacheKey);
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
    @Nullable
    public <T> T get(Object key, Callable<T> valueLoader) {
        log.debug("Get Object from redis by key [{}]",key);

        Object value = lookup(key);
        if(value != null) {
            return (T) value;
        }
        try {
            value = valueLoader.call();
            if(value == null && !isAllowNullValues())
                return null;

            Object storeValue = toStoreValue(value);
            put(key, storeValue);
            return (T)value;
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        }

        return null;
    }

    @Override
    public void put(Object key, @Nullable Object value) {
        log.debug("Put Object to redis [{}] -> [{}]",key,value);

        if (!super.isAllowNullValues() && value == null) {
            this.evict(key);
            return;
        }

        long expire = getExpire();
        if(expire > 0) {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value), expire, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(getKey(key), toStoreValue(value));
        }
    }

    @Override
    @Nullable
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put Object to redis [{}] -> [{}] if it absent",key,value);

        Object cacheKey = getKey(key);
        Object cacheValue = redisTemplate.opsForValue().get(cacheKey);
        if(cacheValue == null) {
            long expire = getExpire();
            if (expire > 0) {
                redisTemplate.opsForValue().set(getKey(key), toStoreValue(value), expire, TimeUnit.MILLISECONDS);
            } else {
                redisTemplate.opsForValue().set(getKey(key), toStoreValue(value));
            }
        }else {
            return toValueWrapper(cacheValue);
        }

        return toValueWrapper(value);
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict Object from redis [{}]",key);

        redisTemplate.delete(getKey(key));
    }

    @Override
    public void clear() {
        log.debug("Clear redis store from by key prefix:{}",this.name.concat(":"));

        Set<Object> keys = redisTemplate.keys(getCachePrefix().concat("*"));
        redisTemplate.delete(keys);
    }

    private long getExpire() {
        long expire = this.cacheProperties.getDefaultExpiration();
        Long cacheNameExpire = cacheProperties.getExpires().get(this.name);
        return cacheNameExpire == null ? expire : cacheNameExpire;
    }

    private Object getKey(Object key) {
        if(key.toString().startsWith(cachePrefix))
            return  key;
        return getCachePrefix().concat(key.toString());
    }

    private String getCachePrefix(){
        return (StringUtils.isEmpty(cachePrefix)?"echo":cachePrefix).concat(":").concat(this.name).concat(":");
    }

    @Override
    public void refresh(Object key) {
        Threads.getExecutorService().submit(() -> {
            //Try to other better way??
            redisTemplate.opsForValue().get(key);
        });
    }
}