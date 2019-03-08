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

package org.echo.xcache.secondary;

import lombok.extern.slf4j.Slf4j;
import org.echo.xcache.message.CacheMessage;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Optional;

/**
 * Redis+CaffeineProperties 基于redis消息机制的消息监听器,用于集群时通知各节点处理缓存
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class RedisBaseCacheMessageListener implements MessageListener {

    private RedisTemplate<Object, Object> redisTemplate;

    private SecondaryCacheManager cacheManager;

    public RedisBaseCacheMessageListener(Optional<RedisTemplate<Object, Object>> redisTemplate,
                                         SecondaryCacheManager cacheManager) {
        super();
        redisTemplate.ifPresent(this::setRedisTemplate);
        this.cacheManager = cacheManager;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        if(this.redisTemplate == null){
            return;
        }

        CacheMessage cacheMessage = (CacheMessage) redisTemplate.getValueSerializer().deserialize(message.getBody());
        log.debug("Receive a redis topic message, clear local cache, the cacheName is {}, the key is {}", cacheMessage.getCacheName(), cacheMessage.getKey());
        if(cacheMessage.isClosed()){
            cacheManager.autoCloseOrOpen(cacheMessage);
        }else{
            cacheManager.clearLocal(cacheMessage);
        }
    }

    private void setRedisTemplate(RedisTemplate<Object, Object> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

}