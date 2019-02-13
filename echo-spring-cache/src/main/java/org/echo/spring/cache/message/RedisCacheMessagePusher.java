package org.echo.spring.cache.message;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * 基于Redis 消息发布者实现
 *
 * @author Liguiqing
 * @since V1.0
 */
public class RedisCacheMessagePusher implements CacheMessagePusher{
    private RedisTemplate<Object, Object> redisTemplate;

    public RedisCacheMessagePusher(RedisTemplate<Object, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void push(String topic, CacheMessage message) {
        redisTemplate.convertAndSend(topic, message);
    }
}