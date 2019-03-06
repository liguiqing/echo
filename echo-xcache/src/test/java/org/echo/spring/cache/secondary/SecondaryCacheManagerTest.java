package org.echo.spring.cache.secondary;

import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.config.CacheConfigurations;
import org.echo.spring.cache.config.RedisCacheConfigurations;
import org.echo.spring.cache.config.SecondaryCacheConfigurations;
import org.echo.spring.cache.message.CacheMessage;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RBlockingDeque;
import org.redisson.api.RedissonClient;
import org.redisson.codec.FstCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.cache.Cache;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ExtendWith(SpringExtension.class)
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                RedisCacheConfigurations.class, SecondaryCacheConfigurations.class, CacheConfigurations.class
        })
)
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml,classpath:/application-redis.yml"})
@DisplayName("Echo : SecondaryCacheManager Test")
public class SecondaryCacheManagerTest extends AbstractConfigurationsTest{

    @Autowired
    private SecondaryCacheManager secondaryCacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${jdbc.driver}")
    private String driver;

    @Autowired
    private RedissonClient redissonClient;

    @Test
    public void test(){
        assertNotNull(driver);
        secondaryCacheManager.removeAllCache();
        String identifier = UUID.randomUUID().toString();
        redisTemplate.convertAndSend("Test",new CacheMessage(identifier,"cache1","c1"));
        assertNotNull(secondaryCacheManager);
        Collection<String> cacheNames = secondaryCacheManager.getCacheNames();
        assertEquals(0,cacheNames.size());

        assertNull(secondaryCacheManager.getCache(null));
        assertNull(secondaryCacheManager.getCache(""));
        assertNull(secondaryCacheManager.getCache("#100#100"));

        Cache aCache = secondaryCacheManager.getCache("aCache#100#100");
        assertNotNull(aCache);
        Cache bCache = secondaryCacheManager.getCache("bCache");
        assertNotNull(bCache);
        String a = aCache.get("a", () -> "a");
        assertNotNull(a);
        a = aCache.get("a", () -> "a");
        assertNotNull(a);
        assertNotNull(aCache.get("a"));
        assertNotNull(aCache.get("a"));
        assertNotNull(aCache.get("a"));
        aCache.clear();
        assertNull(aCache.get("a"));

        a = aCache.get("a", () -> "a");
        redisTemplate.convertAndSend("echo:redis:secondary:topic",new CacheMessage(identifier,"aCache","a"));
        a = aCache.get("a").get()+"";
        assertNotNull(a);
        assertEquals("a",a);
        aCache.clear();

        aCache = secondaryCacheManager.getCache("aCache#100#100#1");
        aCache.get("a", () -> "a");
        assertNotNull(aCache.get("a"));
        assertEquals(aCache.get("a").get(),"a");

        CacheTestBean tb = new CacheTestBean("f1",1,false, LocalDateTime.now());
        CacheTestBean tb_ =  aCache.get(tb,()->tb);
        assertNotNull(tb_);
        assertEquals(tb,tb_);
        aCache.evict(tb);
        assertNull(aCache.get(tb));

        aCache.putIfAbsent(tb, tb);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        aCache.clear();
        assertNull(aCache.get(tb));

        aCache.put(tb, tb);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        tb_ = aCache.get(tb,CacheTestBean.class);
        assertNotNull(tb_);
        aCache.clear();
        assertNull(aCache.get(tb));

        Cache cache1 = secondaryCacheManager.getCache("cache1");
        assertNotNull(cache1);
        assertEquals("cache1",cache1.getName());
        String k1 = cache1.get("k1", () -> "v1");
        assertEquals("v1",k1);
        cache1.clear();
        assertNull(cache1.get("k1"));

        secondaryCacheManager.getCache("cache2");
        cacheNames = secondaryCacheManager.getCacheNames();
        assertEquals(4,cacheNames.size());
        assertTrue(cacheNames.contains("cache1"));

        redisTemplate.convertAndSend("Test","Hello baby");
        String kb1 = bCache.get("k1", () -> "v1");
        cache1.get("k1", () -> "v1");
        cache1.clear();
        assertEquals(kb1,bCache.get("k1").get());

        bCache.get("bc1", () -> "bc1");
        redisTemplate.convertAndSend("echo:redis:secondary:topic",new CacheMessage(identifier,"bCache","bc1"));
        assertEquals(kb1,bCache.get("k1").get());
        assertEquals(kb1,bCache.get("k1").get());
        assertEquals(kb1,bCache.get("k1").get());

        bCache.clear();

        Cache coCache = secondaryCacheManager.getCache("coCache");
        Object o = coCache.get("co1", () -> "co1");
        assertEquals("co1",o);
        secondaryCacheManager.closeAll(1);
        assertEquals(o,coCache.get("co1").get());
        secondaryCacheManager.closeAll(2);
        assertNull(coCache.get("co1"));
        secondaryCacheManager.open("coCache",1);
        assertEquals(o,coCache.get("co1",() -> "co1"));
        secondaryCacheManager.closeAll(1);
        assertNull(coCache.get("co1"));
        secondaryCacheManager.open("coCache",2);
        assertEquals(o,coCache.get("co1",() -> "co1"));
        secondaryCacheManager.open("coCache",1);
        assertEquals(o,coCache.get("co1").get());
        assertEquals(o,coCache.get("co1").get());
        secondaryCacheManager.closeAll(9);
        assertNull(coCache.get("co1"));
        secondaryCacheManager.openAll(9);
        assertEquals(o,coCache.get("co1",() -> "co1"));
        assertEquals(o,coCache.get("co1").get());
        coCache.clear();

        secondaryCacheManager.openAll(-1);
        secondaryCacheManager.closeAll(-1);

        secondaryCacheManager.autoCloseOrOpen(new CacheMessage("id","coCache","a",1));
        secondaryCacheManager.autoCloseOrOpen(new CacheMessage("id","coCache","a",-1));
        secondaryCacheManager.autoCloseOrOpen(new CacheMessage("id","coCache","a",0));

        assertTrue(secondaryCacheManager.hasTwoLevel());

        assertNotNull(secondaryCacheManager.getCache("cN1#1#1#1"));
        assertNotNull(secondaryCacheManager.getCache("cN2#1#1#2"));
        assertNotNull(secondaryCacheManager.getCache("cN3#1#1#-1"));
        assertNotNull(secondaryCacheManager.getCache("cN4#${exec.ttl:1}#1#2"));
        assertNotNull(secondaryCacheManager.getCache("cN5##1#2"));
        assertNotNull(secondaryCacheManager.getCache("cN6#${exec.ttl:-1}#1#2"));
        String id = ((SecondaryCache)coCache).getIdentifier();
        secondaryCacheManager.clearLocal(new CacheMessage(id,"coCache","k"));
        secondaryCacheManager.clearLocal(new CacheMessage("id1","coCache","k"));
        secondaryCacheManager.clearLocal(new CacheMessage("id2","coCache","k"));
        secondaryCacheManager.clearLocal(new CacheMessage("id3","coCache",null));

        SecondaryCacheProperties cacheProperties = spy(new SecondaryCacheProperties());
        when(cacheProperties.isDynamic()).thenReturn(false).thenReturn(true);
        CacheFactory cacheL1Factory = mock(CacheFactory.class);
        CacheFactory cacheL2Factory = mock(CacheFactory.class);
        CacheMessagePusher messagePusher = mock(CacheMessagePusher.class);
        SecondaryCacheManager cacheManager = new SecondaryCacheManager(cacheProperties,cacheL1Factory,cacheL2Factory,messagePusher);

        Cache tCache = cacheManager.getCache("Test");
        assertNull(tCache);
        assertTrue(cacheManager.hasTwoLevel());

        secondaryCacheManager.removeCache("Test");
    }

    @Test
    public void testRedissonCollections(){
        String dequeueKey = "echo:catch:exec:deKey1";
        RBlockingDeque deque = redissonClient.getBlockingDeque(dequeueKey);
        deque.clear();
        CacheTestBean tb1 = new CacheTestBean("f1",1,false, LocalDateTime.now());
        deque.add(tb1);
        RBlockingDeque deque_ = redissonClient.getBlockingDeque(dequeueKey);
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertFalse(deque_.contains(tb1));

        deque.add(tb1);
        deque.add(tb1);
        deque_.remove(tb1);
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertFalse(deque_.contains(tb1));

        deque.add(tb1);
        deque.add(tb1);
        CacheTestBean tb2 = new CacheTestBean("f1",2,false, LocalDateTime.now());
        deque.add(tb2);
        deque.add(tb2);
        deque.add(tb2);
        assertEquals(5,deque_.size());
        deque_.remove(tb1);
        assertEquals(4,deque_.size());
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertEquals(3,deque_.size());
        assertFalse(deque_.contains(tb1));
        deque.clear();

        //证明默认的fastJson 是以属性值来比较对象是否相等的,与hashCode及equals无关
        deque.add(tb1);
        deque.add(tb1);
        deque.addFirst(tb2);
        deque.addFirst(tb2);
        deque.addFirst(tb2);
        assertEquals(5,deque_.size());
        tb1.setF2(2);
        deque_.remove(tb1);
        assertEquals(5,deque_.size());
        assertFalse(deque_.contains(tb1));
        deque_.remove(tb1);
        assertEquals(5,deque_.size());
        assertFalse(deque_.contains(tb1));
        deque.clear();
        deque.delete();


        //使用FstCodec作为序列化及反序列化
        deque = redissonClient.getBlockingDeque(dequeueKey,new FstCodec());
        deque.clear();
        deque.add(tb1);
        deque_ = redissonClient.getBlockingDeque(dequeueKey,new FstCodec());
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertFalse(deque_.contains(tb1));

        deque.add(tb1);
        deque.add(tb1);
        deque_.remove(tb1);
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertFalse(deque_.contains(tb1));

        deque.add(tb1);
        deque.add(tb1);
        deque.add(tb2);
        deque.add(tb2);
        deque.add(tb2);
        assertEquals(5,deque_.size());
        deque_.remove(tb1);
        assertEquals(4,deque_.size());
        assertTrue(deque_.contains(tb1));
        deque_.remove(tb1);
        assertEquals(3,deque_.size());
        assertFalse(deque_.contains(tb1));
        deque.clear();

        //证明使用的FstCodec 是通过hashCode及equals来比较对象的
        deque.add(tb1);
        deque.add(tb1);
        deque.addFirst(tb2);
        deque.addFirst(tb2);
        deque.addFirst(tb2);
        assertEquals(5,deque_.size());
        tb1.setF2(2);
        deque_.remove(tb1);
        assertTrue(deque_.contains(tb1));
        tb1.setF3(false);
        deque_.remove(tb1);
        assertEquals(3,deque_.size());
        assertFalse(deque_.contains(tb1));
        assertTrue(deque_.contains(tb2));
        tb2.setF3(false);
        assertTrue(deque_.contains(tb2));
        tb2.setF1("f2_");
        assertFalse(deque_.contains(tb2));
        deque.remove(tb2);
        assertEquals(3,deque_.size());
        assertEquals(3,deque.size());
        //使用removeIf可以删除符合条件的对象
        deque.removeIf(tb->true);
        assertEquals(0,deque.size());
        deque.clear();

        deque.delete();
    }

}