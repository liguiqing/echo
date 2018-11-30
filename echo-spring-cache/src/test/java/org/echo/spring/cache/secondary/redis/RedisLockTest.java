package org.echo.spring.cache.secondary.redis;

import com.google.common.collect.Queues;
import com.google.common.collect.Sets;
import org.echo.spring.cache.redis.RedisLock;
import org.echo.spring.cache.secondary.SecondaryCacheAutoConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnection;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

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
@DisplayName("Echo : Redis lock test")
public class RedisLockTest {

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private JedisConnectionFactory jedisConnectionFactory;

    @Autowired
    private JedisPool jedisPool;

    private String prefix = "echo:test:";

    @Test
    public void test(){
        //Jedis jedis = jedisPool.getResource();
        assertNotNull(redisTemplate);

        int i = 0;
        Set<String> keys = Sets.newHashSet();
        while(i<100){
            i++;
            RedisLockTestBean testBean = new RedisLockTestBean((i+1000)*1l,i+"");
            redisTemplate.opsForValue().set(prefix.concat(testBean.toString()),testBean,1000*60*5, TimeUnit.MILLISECONDS);
            keys.add(prefix.concat(testBean.toString()));
        }

        RedisLock locker = new RedisLock();


        Runnable[] runs = new Runnable[10];
        CountDownLatch countDownLatch = new CountDownLatch(keys.size());
        for(int t=0;t<runs.length;t++){
            final int mid = t;
            runs[t] = ()->{
                String maker = "maker" + mid;
                Iterator<String> it = keys.iterator();
                while(it.hasNext()){
                    String key = it.next();
                    String identifier = UUID.randomUUID().toString();
                    boolean b = locker.lock(jedisPool,key,identifier,5);
                    if(b){
                        Object o = redisTemplate.opsForValue().get(key);
                        RedisLockTestBean tb = (RedisLockTestBean)o;
                        if(tb == null || !tb.isUndo())
                            continue;

                        tb.fetch();
                        tb.scored(maker,5d);
                        redisTemplate.opsForValue().set(key,tb,1000*60*5, TimeUnit.MILLISECONDS);
                        countDownLatch.countDown();
                        locker.unlock(jedisPool, key, identifier);
                        try {
                            int random = getRandomNumberInRange(100,200);
                            Thread.sleep(random);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }

        Arrays.asList(runs).forEach(r->new Thread(r).start());

        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Collection c = redisTemplate.opsForValue().multiGet(keys);
        redisTemplate.delete(keys);
        Iterator it = c.iterator();
        while(it.hasNext()){
            RedisLockTestBean tb = (RedisLockTestBean)it.next();
            assertAll("test",()->{
                assertEquals(1,tb.getMakers().size());
                assertTrue(tb.getScore().compareTo(5d) == 0);
            });

        }

    }


    private  int getRandomNumberInRange(int min, int max) {

        Random r = new Random();
        return r.ints(min, (max + 1)).findFirst().getAsInt();
    }
}