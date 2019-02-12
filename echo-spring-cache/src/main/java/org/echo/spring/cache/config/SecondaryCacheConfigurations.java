package org.echo.spring.cache.config;

import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.caffeine.CaffeineCacheFactory;
import org.echo.spring.cache.caffeine.CaffeineCacheProperties;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.echo.spring.cache.secondary.RedisBaseCacheMessageListener;
import org.echo.spring.cache.secondary.SecondaryCacheManager;
import org.echo.spring.cache.secondary.SecondaryCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Optional;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Configuration
@PropertySource(value={"classpath:/application-cache.yml"})
@EnableConfigurationProperties(
        value = {
                SecondaryCacheProperties.class,
                CaffeineCacheProperties.class
        })
public class SecondaryCacheConfigurations {

    @Autowired
    private SecondaryCacheProperties cacheProperties;

    private CacheFactory secondaryCacheFactory;

    private CacheMessagePusher messagePusher;

    @Bean("SecondaryCacheManager")
    public SecondaryCacheManager cacheManager(Optional<CacheFactory> secondaryCacheFactory,
                                              Optional<CacheMessagePusher> messagePusher,
                                              CaffeineCacheProperties caffeineCacheProperties) {
        CaffeineCacheFactory caffeineCacheFactory = new CaffeineCacheFactory(caffeineCacheProperties);
        setSecondaryCacheFactory(caffeineCacheFactory);
        setMessagePusher((a,b)->{});
        secondaryCacheFactory.ifPresent(this::setSecondaryCacheFactory);
        messagePusher.ifPresent(this::setMessagePusher);
        return new SecondaryCacheManager(this.cacheProperties, caffeineCacheFactory,this.secondaryCacheFactory,this.messagePusher);
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
    public RedisMessageListenerContainer redisMessageListenerContainer(Optional<RedisTemplate<Object, Object>> redisTemplate,
                                                                       SecondaryCacheManager cacheManager) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisTemplate.ifPresent(t->redisMessageListenerContainer.setConnectionFactory(t.getConnectionFactory()));
        RedisBaseCacheMessageListener cacheMessageListener = new RedisBaseCacheMessageListener(redisTemplate, cacheManager);
        redisMessageListenerContainer.addMessageListener(cacheMessageListener, new ChannelTopic(cacheProperties.getCacheMessageTopic()));
        return redisMessageListenerContainer;
    }

    private void setSecondaryCacheFactory(CacheFactory secondaryCacheFactory){
        this.secondaryCacheFactory = secondaryCacheFactory;
    }

    private void setMessagePusher(CacheMessagePusher messagePusher){
        this.messagePusher = messagePusher;
    }
}