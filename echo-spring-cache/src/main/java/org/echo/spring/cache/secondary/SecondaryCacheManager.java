package org.echo.spring.cache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.lock.DistributedLock;
import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.message.CacheMessage;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.util.NumbersUtil;
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
 * 两级缓存管理器.
 * 实现了在Spring Cache配置中对key进行超时及空闲时间控制(时间单位:秒).<br/>
 * 使用超时或者空闲时间配置方式如下:<br/>
 * <pre>
 *   <code>@Cacheable(value = "sample#3600#3600",key = "#p0.id",unless = "#result == null")</code>
 * or
 *   <code>@Cacheable(value = "sample#${sample.cache.ttl:3600}#${sample.cache.maxIdleSecond:3600}",key = "#p0.id",unless = "#result == null")</code>
 * </pre>
 * 实现了只启用其中一个缓存：在ttl配置再加入一个配置值，1-只启用一级缓存；2-只启用2级缓存；其他值无效
 * <code>@Cacheable(value = "sample#3600#3600＃1",key = "#p0.id",unless = "#result == null")</code>
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class SecondaryCacheManager extends AbstractTransactionSupportingCacheManager {

    /**
     * <pre>
     * 缓存参数的分隔符
     * 数组元素0=缓存的名称
     * 数组元素1=缓存过期时间TTL(秒)
     * 数组元素2=缓存在多少秒开始主动失效来强制刷新(秒),如果不存在使用数组元素1的值
     * 数组元素3=如果存在,值仅可为1 or 2,其他值视为无效.
     * sample:
     * cacheName#360#120
     *    or
     * cacheName#360#120#1
     * </pre>
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

    private ConcurrentMap<String, SecondaryCache> cacheMap = new ConcurrentHashMap<>();

    private CacheFactory cacheL1Factory;

    private CacheFactory cacheL2Factory;

    private CacheMessagePusher messagePusher;

    @Autowired
    private DefaultListableBeanFactory beanFactory;

    @Autowired(required = false)
    private DistributedLock lock;

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

        SecondaryCache cache = cacheMap.get(cacheName);
        if(cache != null) {
            log.debug("Return exist cache ->{}:{}",cacheName,cache);
            return cache;
        }

        if(!dynamic) {
            return null;
        }

        // 有效时间，初始化获取默认的有效时间
        long ttl = getTtl(cacheParams);
        // 访问过期时间
        long maxIdleSecond = getMaxIdleSecond(cacheParams);
        int level = getLevel(cacheParams);

        cache = createCache(cacheName, ttl, maxIdleSecond,level);
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
        SecondaryCache cache = cacheMap.get(cacheName);
        if(cache == null) {
            return ;
        }

        if(!message.sameOfIdentifier(cache.getIdentifier()))
            cache.clearLocal(key);
    }

    public boolean hasTwoLevel(){
        return cacheL2Factory != null;
    }

    private SecondaryCache createCache(String cacheName,long expirationSecondTime,long expireAfterAccessSecondTime,int level){
        if(level == 1)
            return SecondaryCache.onlyCache1(cacheName,cacheL1Factory.newCache(cacheName,expirationSecondTime,expireAfterAccessSecondTime),cacheProperties,lock);

        if(level == 2)
            return SecondaryCache.onlyCache2(cacheName,cacheL2Factory.newCache(cacheName,expirationSecondTime,expireAfterAccessSecondTime),cacheProperties,lock);

        return new SecondaryCache(cacheName,
                cacheL1Factory.newCache(cacheName,expirationSecondTime,expireAfterAccessSecondTime),
                cacheL2Factory.newCache(cacheName,expirationSecondTime,expireAfterAccessSecondTime),
                cacheProperties,messagePusher,lock);
    }

    /**
     * 获取过期时间
     * @param cacheParams　cacheParams
     * @return
     */
    private long getTtl(String[] cacheParams) {

        // 设置key有效时间
        if (cacheParams.length > 1) {
            return toLong(cacheParams[1]);
        }
        // 默认是0
        return cacheProperties.getDefaultTtl();
    }

    /**
     * 获取访问后过期时间
     *
     * @param cacheParams　cacheParams
     * @return　millsecond
     */
    private long getMaxIdleSecond(String[] cacheParams) {
        if (cacheParams.length > 2) {
            return toLong(cacheParams[2]);
        }
        // 默认是0
        return cacheProperties.getDefaultTtl();
    }

    private long toLong(String s){
        if (!StringUtils.isEmpty(s)) {
            // 支持配置刷新时间使用EL表达式读取配置文件时间
            if (s.contains(MARK)) {
                s = beanFactory.resolveEmbeddedValue(s);
            }
        }
        return NumbersUtil.stringToLong(s);
    }

    /**
     * 启用级别
     *
     * @param cacheParams　cacheParams
     * @return　if 1 only cache level1 or 2 only cache level2 otherwise both
     */
    private int getLevel(String[] cacheParams){
        if(cacheParams.length == 4){
            return NumbersUtil.stringToInt(cacheParams[3]);
        }

        return 0;
    }
}