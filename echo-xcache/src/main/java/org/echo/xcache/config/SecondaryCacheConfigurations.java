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

package org.echo.xcache.config;

import lombok.extern.slf4j.Slf4j;
import org.echo.xcache.CacheFactory;
import org.echo.xcache.binary.BinaryCacheManager;
import org.echo.xcache.caffeine.CaffeineCacheFactory;
import org.echo.xcache.caffeine.CaffeineCacheProperties;
import org.echo.xcache.message.CacheMessagePusher;
import org.echo.xcache.binary.RedisBaseCacheMessageListener;
import org.echo.xcache.binary.BinaryCacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Optional;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(
        value = {
                BinaryCacheProperties.class,
                CaffeineCacheProperties.class
        })
public class SecondaryCacheConfigurations {

    @Autowired
    private BinaryCacheProperties cacheProperties;

    private CacheFactory secondaryCacheFactory;

    private CacheMessagePusher messagePusher;

    @Bean("SecondaryCacheManager")
    public BinaryCacheManager cacheManager(Optional<CacheFactory> secondaryCacheFactory,
                                           Optional<CacheMessagePusher> messagePusher,
                                           CaffeineCacheProperties caffeineCacheProperties) {
        CaffeineCacheFactory caffeineCacheFactory = new CaffeineCacheFactory(caffeineCacheProperties);
        setSecondaryCacheFactory(caffeineCacheFactory);
        setMessagePusher((topic,message)->log.debug("Publish nothing of {}",topic));
        secondaryCacheFactory.ifPresent(this::setSecondaryCacheFactory);
        messagePusher.ifPresent(this::setMessagePusher);
        return new BinaryCacheManager(this.cacheProperties, caffeineCacheFactory,
                this.secondaryCacheFactory,this.messagePusher);
    }


    /**
     * 注册一个基于Redis的缓存消息处理器,可以替换为其他消息中间件来实现相同的功能
     *
     * @param redisTemplate RedisTemplate
     * @param cacheManager CacheManager
     * @return RedisMessageListenerContainer
     */
    @Bean
    @ConditionalOnBean(BinaryCacheManager.class)
    public RedisMessageListenerContainer redisMessageListenerContainer(Optional<RedisTemplate<Object, Object>> redisTemplate,
                                                                       BinaryCacheManager cacheManager) {
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