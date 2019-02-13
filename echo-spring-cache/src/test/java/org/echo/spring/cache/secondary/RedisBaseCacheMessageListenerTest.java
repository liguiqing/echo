package org.echo.spring.cache.secondary;

import org.echo.spring.cache.CacheFactory;
import org.echo.spring.cache.message.CacheMessage;
import org.echo.spring.cache.message.CacheMessagePusher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Optional;

import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : RedisBaseCacheMessageListener Test")
class RedisBaseCacheMessageListenerTest {

    @Test
    void onMessage() {
        RedisTemplate redisTemplate = spy(new RedisTemplate());
        SecondaryCacheProperties cacheProperties = new SecondaryCacheProperties();
        CacheFactory cacheL1Factory = mock(CacheFactory.class);
        CacheFactory cacheL2Factory = mock(CacheFactory.class);
        CacheMessagePusher messagePusher = mock(CacheMessagePusher.class);
        CacheMessage cacheMessage = mock(CacheMessage.class);
        RedisSerializer redisSerializer = mock(RedisSerializer.class);
        when(redisTemplate.getValueSerializer()).thenReturn(redisSerializer);
        when(redisSerializer.deserialize(any(byte[].class))).thenReturn(cacheMessage);
        when(cacheMessage.isClosed()).thenReturn(true).thenReturn(false);
        SecondaryCacheManager cacheManager = spy(new SecondaryCacheManager(cacheProperties, cacheL1Factory, cacheL2Factory, messagePusher));
        doNothing().when(cacheManager).autoCloseOrOpen(any(CacheMessage.class));
        doNothing().when(cacheManager).clearLocal(any(CacheMessage.class));

        RedisBaseCacheMessageListener messageListener = new RedisBaseCacheMessageListener(Optional.of(redisTemplate), cacheManager);
        Message message = mock(Message.class);
        when(message.getBody()).thenReturn(new byte[]{});
        messageListener.onMessage(message,new byte[]{});
        messageListener.onMessage(message,new byte[]{});
        messageListener.onMessage(message,new byte[]{});

        messageListener = new RedisBaseCacheMessageListener(Optional.empty(), cacheManager);
        messageListener.onMessage(message,new byte[]{});
    }
}