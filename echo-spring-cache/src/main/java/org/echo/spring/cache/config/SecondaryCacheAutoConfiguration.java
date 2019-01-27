package org.echo.spring.cache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.lock.DistributedLock;
import org.echo.lock.RedisBaseDistributedLock;
import org.echo.spring.cache.CacheDequeFactory;
import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.caffeine.CaffeineCacheFactory;
import org.echo.spring.cache.caffeine.CaffeineCacheProperties;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.spring.cache.message.RedisCacheMessagePusher;
import org.echo.spring.cache.redis.RedisCacheFactory;
import org.echo.spring.cache.redis.RedisCacheProperties;
import org.echo.spring.cache.redis.RedissonCacheDequeFactory;
import org.echo.spring.cache.secondary.RedisBaseCacheMessageListener;
import org.echo.spring.cache.secondary.SecondaryCacheManager;
import org.echo.spring.cache.secondary.SecondaryCacheProperties;
import org.echo.util.RedisClientUtils;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

/**
 * 二级缓存自动配置
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@PropertySource(value={"classpath:/application-cache.yml"})
@EnableConfigurationProperties(
    value = {
        SecondaryCacheProperties.class,
        CaffeineCacheProperties.class,
        RedisCacheProperties.class
    })
public class SecondaryCacheAutoConfiguration {
    @Autowired
    private SecondaryCacheProperties cacheProperties;

    @Autowired
    private CaffeineCacheProperties caffeineCacheProperties;

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson(){
        org.redisson.config.Config config = new org.redisson.config.Config();
        config.useSingleServer().setAddress("redis://".concat(redisCacheProperties.getStandalone().toHost()));
        return Redisson.create(config);
    }

    @Bean
    public DistributedLock distributedLock(RedissonClient redissonClient, @Value("${app.util.lock.prefix:echo}")String lockPrefix){
        return new RedisBaseDistributedLock(lockPrefix, redissonClient);
    }

    @Bean
    public CacheDequeFactory cacheDequeFactory(RedissonClient redissonClient){
        if(RedisClientUtils.isAlive(redissonClient)){
            return new RedissonCacheDequeFactory(redissonClient, redisCacheProperties);
        }
        return new CacheDequeFactory(){};
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory connectionFactory){
        RedisTemplate<Object, Object> redisTemplate =  new RedisTemplate<>();
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        return new RedisClusterConfiguration(redisCacheProperties.getHostsAndPorts());
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisCacheProperties.getStandalone().getHost(),
                redisCacheProperties.getStandalone().getPort()));
    }

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRunsMillis(30000);
        config.setNumTestsPerEvictionRun(10);
        config.setMinEvictableIdleTimeMillis(60000);
        return new JedisPool(config,redisCacheProperties.getStandalone().getHost(),
                redisCacheProperties.getStandalone().getPort());
    }

    @Bean("SecondaryCacheManager")
    @ConditionalOnBean(RedisTemplate.class)
    public SecondaryCacheManager cacheManager(RedissonClient redissonClient, RedisTemplate<Object, Object> redisTemplate) {
        CacheFactory caffeineCacheFactory = new CaffeineCacheFactory(caffeineCacheProperties);
        CacheFactory redisCacheFactory = new RedisCacheFactory(redissonClient,redisCacheProperties);
        CacheMessagePusher messagePusher = new RedisCacheMessagePusher(redisTemplate);
        if(!RedisClientUtils.isAlive(redissonClient)){
            redisCacheFactory = caffeineCacheFactory;
            messagePusher = (topic,message)->{};
        }
        return new SecondaryCacheManager(cacheProperties, caffeineCacheFactory,redisCacheFactory,messagePusher);
    }

    /**
     * 注册一个基于Redis的缓存消息处理器,可以替换为其他消息中间件来实现相同的功能
     *
     * @param redisTemplate RedisTemplate
     * @param cacheManager CacheManager
     * @return RedisMessageListenerContainer
     */
    @Bean
    @ConditionalOnBean(SecondaryCacheManager.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate,
                                                                       SecondaryCacheManager cacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(Objects.requireNonNull(redisTemplate.getConnectionFactory()));
        RedisBaseCacheMessageListener cacheMessageListener = new RedisBaseCacheMessageListener(redisTemplate, cacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(cacheProperties.getCacheMessageTopic()));
        return redisMessageListenerContainer;
    }
}