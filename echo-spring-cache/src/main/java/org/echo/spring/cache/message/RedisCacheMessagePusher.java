package org.echo.spring.cache.message;

import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author Liguiqing
 * @since V3.0
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