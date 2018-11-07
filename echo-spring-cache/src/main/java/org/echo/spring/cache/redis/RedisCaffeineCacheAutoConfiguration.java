package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@Configuration
@AutoConfigureAfter(RedisAutoConfiguration.class)
@EnableConfigurationProperties(RedisCaffeineCacheProperties.class)
public class RedisCaffeineCacheAutoConfiguration {
    @Autowired
    private RedisCaffeineCacheProperties redisCaffeineCacheProperties;

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory connectionFactory){
        RedisTemplate<Object, Object> redisTemplate =  new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    @ConfigurationProperties(prefix="spring.redis")
    public RedisClusterConfiguration redisClusterConfiguration(){
        return new RedisClusterConfiguration();
    }

    @Bean

    public JedisConnectionFactory redisConnectionFactory(RedisClusterConfiguration clusterConfiguration){
        return new JedisConnectionFactory(clusterConfiguration);
    }

    @Bean
    @ConditionalOnBean(RedisTemplate.class)
    public RedisCaffeineCacheManager cacheManager(RedisTemplate<Object, Object> redisTemplate) {
        return new RedisCaffeineCacheManager(redisCaffeineCacheProperties, redisTemplate);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisTemplate<Object, Object> redisTemplate,
                                                                       RedisCaffeineCacheManager redisCaffeineCacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisTemplate.getConnectionFactory());
        RedisBaseCacheMessageListener cacheMessageListener = new RedisBaseCacheMessageListener(redisTemplate, redisCaffeineCacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(redisCaffeineCacheProperties.getRedis().getTopic()));
        return redisMessageListenerContainer;
    }
}