package org.echo.spring.cache.secondary;

import org.echo.spring.cache.message.CacheMessage;
import org.echo.test.config.AbstractConfigurationsTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author Liguiqing
 * @since V1.0
 */
@ExtendWith(SpringExtension.class)
@ContextHierarchy(@ContextConfiguration(
        initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {
                SecondaryCacheAutoConfiguration.class
        })
)
@TestPropertySource(properties = {"spring.config.location = classpath:/application-cache.yml"})
@DisplayName("Echo : SecondaryCacheManager test")
public class SecondaryCacheManagerTest extends AbstractConfigurationsTest{

    @Autowired
    private SecondaryCacheManager secondaryCacheManager;

    @Autowired
    private RedisTemplate redisTemplate;

    @Value("${jdbc.driver}")
    private String driver;


    @Test
    public void test(){
        assertNotNull(driver);
        redisTemplate.convertAndSend("Test",new CacheMessage("cache1","c1"));
        assertNotNull(secondaryCacheManager);
        Collection<String> cacheNames = secondaryCacheManager.getCacheNames();
        assertEquals(0,cacheNames.size());

        Cache aCache = secondaryCacheManager.getCache("aCache");
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
        redisTemplate.convertAndSend("echo:redis:secondary:topic",new CacheMessage("bCache","bc1"));
        assertEquals(kb1,bCache.get("k1").get());
        assertEquals(kb1,bCache.get("k1").get());
        assertEquals(kb1,bCache.get("k1").get());

        bCache.clear();
    }

}