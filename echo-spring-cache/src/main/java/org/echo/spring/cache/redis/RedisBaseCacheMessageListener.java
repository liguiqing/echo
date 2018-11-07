package org.echo.spring.cache.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * Redis+Caffeine 基于redis消息机制的消息监听器,用于集群时通知各节点处理缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisBaseCacheMessageListener implements MessageListener {

    private RedisTemplate<Object, Object> redisTemplate;

    private RedisCaffeineCacheManager redisCaffeineCacheManager;

    public RedisBaseCacheMessageListener(RedisTemplate<Object, Object> redisTemplate,
                                         RedisCaffeineCacheManager redisCaffeineCacheManager) {
        super();
        this.redisTemplate = redisTemplate;
        this.redisCaffeineCacheManager = redisCaffeineCacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("Receive a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        redisCaffeineCacheManager.clearLocal(cacheMessage.getCacheName(), cacheMessage.getKey());
    }

}