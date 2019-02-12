package org.echo.spring.cache;

import org.echo.lock.DistributedLock;
import org.echo.spring.cache.caffeine.CaffeineCaches;
import org.echo.spring.cache.caffeine.CaffeineProperties;
import org.echo.spring.cache.config.CacheConfigurations;
import org.echo.spring.cache.config.RedisCacheConfigurations;
import org.echo.spring.cache.config.SecondaryCacheConfigurations;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.spring.cache.redis.RedisCacheFactory;
import org.echo.spring.cache.redis.RedisCacheProperties;
import org.echo.spring.cache.redis.RedisNoneCache;
import org.echo.spring.cache.secondary.SecondaryCache;
import org.echo.spring.cache.secondary.SecondaryCacheProperties;
import org.echo.test.PrivateConstructors;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@ExtendWith(SpringExtension.class)
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisCacheConfigurations.class, SecondaryCacheConfigurations.class, CacheConfigurations.class
        })
)
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@DisplayName("NativeCaches Test")
class NativeCachesTest extends AbstractConfigurationsTest {

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    @Autowired
    private SecondaryCacheProperties secondaryCacheProperties;

    @Test
    void test() {
        assertEquals(0,NativeCaches.size(null));
        assertEquals(0,NativeCaches.size(new RedisNoneCache(true)));
        CacheTestKey k1 = new CacheTestKey("k1");
        CacheTestKey k2 = new CacheTestKey("k2");
        CacheTestValue v1 = new CacheTestValue().setV1("v1").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheTestValue v2 = new CacheTestValue().setV1("v2").setV2(false).setV3(1).setV4(LocalDate.now());
        CacheTestValue v11 = new CacheTestValue().setV1("v11").setV2(true).setV3(11);
        v1.addChild(v11);

        CaffeineCache  caffeineCache = new CaffeineCache("exec",CaffeineCaches.newCache(new CaffeineProperties()));
        assertEquals(0,NativeCaches.size(caffeineCache));
        caffeineCache.put(k1,v1);
        assertEquals(1,NativeCaches.size(caffeineCache));
        assertTrue(NativeCaches.keys(caffeineCache).contains(k1));
        assertFalse(NativeCaches.keys(caffeineCache).contains(k2));
        assertTrue(NativeCaches.values(caffeineCache).contains(v1));
        assertFalse(NativeCaches.values(caffeineCache).contains(v2));

        RedisCacheFactory factory = new RedisCacheFactory(redissonClient,redisCacheProperties);
        Cache redissonCache = factory.newCache("exec", 10, 10);
        redissonClient.getMap(redissonCache.getName()).delete();
        assertEquals(0,NativeCaches.size(redissonCache));
        redissonCache.put(k1,v1);
        assertEquals(1,NativeCaches.size(redissonCache));
        assertTrue(NativeCaches.keys(redissonCache).contains(k1));
        assertFalse(NativeCaches.keys(redissonCache).contains(k2));
        assertTrue(NativeCaches.values(redissonCache).contains(v1));
        assertFalse(NativeCaches.values(redissonCache).contains(v2));
        redissonCache.evict(k1);
        redissonClient.getMap(redissonCache.getName()).delete();
        DistributedLock lock = mock(DistributedLock.class);

        SecondaryCache sCache = SecondaryCache.onlyCache1("exec", caffeineCache, secondaryCacheProperties, lock);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        sCache = SecondaryCache.onlyCache2("exec", caffeineCache, secondaryCacheProperties, lock);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        CacheMessagePusher pusher = mock(CacheMessagePusher.class);
        sCache = new SecondaryCache("exec", caffeineCache,redissonCache, secondaryCacheProperties,pusher);
        assertEquals(1,NativeCaches.size(sCache));
        assertTrue(NativeCaches.keys(sCache).contains(k1));
        assertFalse(NativeCaches.keys(sCache).contains(k2));
        assertTrue(NativeCaches.values(sCache).contains(v1));
        assertFalse(NativeCaches.values(sCache).contains(v2));

        Cache noneCache = new RedisNoneCache(true);
        assertEquals(0,NativeCaches.size(noneCache));
        assertFalse(NativeCaches.keys(noneCache).contains(k1));
        assertFalse(NativeCaches.keys(noneCache).contains(k2));
        assertFalse(NativeCaches.values(noneCache).contains(v1));
        assertFalse(NativeCaches.values(noneCache).contains(v2));

        assertThrows(Exception.class,()->new PrivateConstructors().exec(CaffeineCaches.class));
    }
}