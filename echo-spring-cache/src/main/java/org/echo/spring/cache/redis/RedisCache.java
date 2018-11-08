package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.AbstractMultiAdaptingCache;
import org.echo.util.CollectionsUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.StringUtils;

import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisCache extends AbstractMultiAdaptingCache {

    private String name;

    private RedisCacheProperties cacheProperties;

    private RedisTemplate<Object, Object> redisTemplate;

    public RedisCache(String name,RedisTemplate<Object, Object> redisTemplate, RedisCacheProperties cacheProperties) {
        super(cacheProperties.isCacheNullValues());
        this.name = name;
        this.redisTemplate = redisTemplate;
        this.cacheProperties = cacheProperties;
    }

    @Override
    protected Object lookup(Object key) {
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
    protected void doPut(Object key, Object value) {
        log.debug("Put cache to redis of {}->{}",key,value);

        Object cacheKey = getKey(key);
        long expire = getExpire();
        if(expire > 0) {
            redisTemplate.opsForValue().set(cacheKey, toStoreValue(value), expire, TimeUnit.MILLISECONDS);
        } else {
            redisTemplate.opsForValue().set(cacheKey, toStoreValue(value));
        }
    }

    @Override
    public ValueWrapper putIfAbsent(Object key, Object value) {
        log.debug("Put cache to redis of {}->{} if absent",key,value);

        Object cacheKey = getKey(key);
        Object prevValue =  redisTemplate.opsForValue().get(cacheKey);
        return toValueWrapper(prevValue);
    }

    @Override
    public void evict(Object key) {
        log.debug("Evict cache from redis {}",key);

        redisTemplate.delete(getKey(key));
    }

    @Override
    public void clear() {
        log.debug("Clear from redis ");

        Set<Object> keys = redisTemplate.keys(this.name.concat(":"));
        if(CollectionsUtil.isNotNullAndNotEmpty(keys)){
            keys.forEach(key->redisTemplate.delete(key));
        }
    }

    private long getExpire() {
        Long cacheNameExpire = cacheProperties.getExpires().get(this.name);
        return cacheNameExpire == null ? cacheProperties.getDefaultExpiration() : cacheNameExpire;
    }

    private Object getKey(Object key) {
        return this.name.concat(":").concat(StringUtils.isEmpty(cacheProperties.getCachePrefix()) ? key.toString() : cacheProperties.getCachePrefix().concat(":").concat(key.toString()));
    }
}