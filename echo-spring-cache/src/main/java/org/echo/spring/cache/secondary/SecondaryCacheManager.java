package org.echo.spring.cache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.message.CacheMessage;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.util.StringUtils;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 *
 * 两级缓存管理器,同时实现了在Spring Cache配置中对key进行超时及空闲时间控制(时间单位:秒).<br/>
 * 使用超时或者空闲时间配置方式如下:<br/>
 * <pre>
 *   <code>@Cacheable(value = "sample#3600#3600",key = "#p0.id",unless = "#result == null")</code>
 * or
 *   <code>@Cacheable(value = "sample#${sample.cache.ttl:3600}#${sample.cache.maxIdleSecond:3600}",key = "#p0.id",unless = "#result == null")</code>
 * </pre>
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class SecondaryCacheManager extends AbstractTransactionSupportingCacheManager {

    /**
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL(秒)
     * 数组元素2=空闲时间(秒)
     * sample:cacheName#360#120
     */
    private static final String SEPARATOR = "#";

    /**
     * SpEL标示符
     * 支持SEPARATOR 数组元素1或者2使用el表达式
     * cacheName:#${select.cache.timeout:1800}#${select.cache.refresh:600}
     */
    private static final String MARK = "$";

    private SecondaryCacheProperties cacheProperties;

    private boolean dynamic;

    private ConcurrentMap<String, Cache> cacheMap = new ConcurrentHashMap<>();

    private CacheFactory cacheL1Factory;

    private CacheFactory cacheL2Factory;

    private CacheMessagePusher messagePusher;

    @Autowired
    DefaultListableBeanFactory beanFactory;

    public SecondaryCacheManager(SecondaryCacheProperties cacheProperties,
                                 CacheFactory cacheL1Factory,
                                 CacheFactory cacheL2Factory,
                                 CacheMessagePusher messagePusher) {
        this.cacheProperties = cacheProperties;
        this.dynamic = cacheProperties.isDynamic();
        this.cacheL1Factory = cacheL1Factory;
        this.cacheL2Factory = cacheL2Factory;
        this.messagePusher = messagePusher;
    }

    @Override
    protected Collection<? extends Cache> loadCaches() {
        return cacheMap.values();
    }

    @Override
    public Cache getCache(String name) {
        String[] cacheParams = name.split(SEPARATOR);
        String cacheName = cacheParams[0];
        if (StringUtils.isEmpty(cacheName)) {
            return null;
        }

        // 有效时间，初始化获取默认的有效时间
        long ttl = getTtl(cacheName, cacheParams);
        // 自动刷新时间，默认是0
        long maxIdleSecond = getMaxIdleSecond(cacheParams);

        Cache cache = cacheMap.get(cacheName);
        if(cache != null) {
            log.debug("Return exist cache ->{}:{}",cacheName,cache);
            return cache;
        }

        if(!dynamic) {
            return null;
        }

        cache = new SecondaryCache(cacheName,cacheL1Factory.newCache(cacheName,ttl,maxIdleSecond),
                cacheL2Factory.newCache(cacheName,ttl,maxIdleSecond),cacheProperties,messagePusher);
        Cache oldCache = cacheMap.putIfAbsent(cacheName, cache);
        log.debug("Create cache instance, the cache name is : {}", cacheName);
        return oldCache == null ? cache : oldCache;
    }

    @Override
    public Collection<String> getCacheNames() {
        return this.cacheMap.keySet();
    }
    public void clearLocal(CacheMessage message) {
        log.debug("Clear local {} cache",message.getCacheName());

        String cacheName = message.getCacheName();
        Object key = message.getKey();
        Cache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }

        SecondaryCache secondaryCache = (SecondaryCache) cache;
        if(!message.sameOfIdentifier(secondaryCache.getIdentifier()))
            secondaryCache.clearLocal(key);
    }

    public boolean hasTwoLevel(){
        return cacheL2Factory != null;
    }

    /**
     * 获取过期时间
     *
     * @return
     */
    private long getTtl(String cacheName, String[] cacheParams) {
        // 有效时间，初始化获取默认的有效时间
        Long ttl = cacheProperties.getDefaultTtl();

        // 设置key有效时间
        if (cacheParams.length > 1) {
            String ttlStr = cacheParams[1];
            if (!StringUtils.isEmpty(ttlStr)) {
                // 支持配置过期时间使用EL表达式读取配置文件时间
                if (ttlStr.contains(MARK)) {
                    ttlStr = beanFactory.resolveEmbeddedValue(ttlStr);
                }
                ttl = Long.parseLong(ttlStr);
            }
        }

        return ttl;
    }

    /**
     * 获取自动刷新时间
     *
     * @return
     */
    private long getMaxIdleSecond(String[] cacheParams) {
        // 自动刷新时间，默认是0
        Long maxIdleSecond = 0L;
        // 设置自动刷新时间
        if (cacheParams.length > 2) {
            String preloadStr = cacheParams[2];
            if (!StringUtils.isEmpty(preloadStr)) {
                // 支持配置刷新时间使用EL表达式读取配置文件时间
                if (preloadStr.contains(MARK)) {
                    preloadStr = beanFactory.resolveEmbeddedValue(preloadStr);
                }
                maxIdleSecond = Long.parseLong(preloadStr);
            }
        }
        return maxIdleSecond;
    }

}