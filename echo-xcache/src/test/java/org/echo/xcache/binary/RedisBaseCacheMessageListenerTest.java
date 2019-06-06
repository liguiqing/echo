/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.echo.xcache.binary;

import org.echo.xcache.CacheFactory;
import org.echo.xcache.message.CacheMessage;
import org.echo.xcache.message.CacheMessagePusher;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@DisplayName("Echo : RedisBaseCacheMessageListener Test")
class RedisBaseCacheMessageListenerTest {

    @Test
    void onMessage() {
        RedisTemplate redisTemplate = spy(new RedisTemplate());
        BinaryCacheProperties cacheProperties = new BinaryCacheProperties();
        CacheFactory cacheL1Factory = mock(CacheFactory.class);
        CacheFactory cacheL2Factory = mock(CacheFactory.class);
        CacheMessagePusher messagePusher = mock(CacheMessagePusher.class);
        CacheMessage cacheMessage = mock(CacheMessage.class);
        RedisSerializer redisSerializer = mock(RedisSerializer.class);
        when(redisTemplate.getValueSerializer()).thenReturn(redisSerializer);
        when(redisSerializer.deserialize(any(byte[].class))).thenReturn(cacheMessage);
        when(cacheMessage.isClosed()).thenReturn(true).thenReturn(false);
        BinaryCacheManager cacheManager = Mockito.spy(new BinaryCacheManager(cacheProperties, cacheL1Factory, cacheL2Factory, messagePusher));
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
        assertTrue(true);
    }
}