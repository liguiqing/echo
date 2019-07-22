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

import org.echo.messaging.MessagePublish;
import org.echo.xcache.CacheDequeFactory;
import org.echo.xcache.XCacheProperties;
import org.echo.xcache.binary.BinaryCacheMessageConsume;
import org.echo.xcache.message.CacheMessage;
import org.echo.xcache.redis.RedisCacheFactory;
import org.echo.xcache.redis.RedissonCacheDequeFactory;
import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

/**
 * 二级缓存自动配置
 *
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@ConditionalOnProperty("spring.redis")
@ConditionalOnClass(RedisOperations.class)
@EnableConfigurationProperties(value = XCacheProperties.class)
public class RedisCacheConfigurations {

    @Bean
    @ConditionalOnMissingBean(name="cacheMessagePublish")
    @ConditionalOnClass(RedisTemplate.class)
    MessagePublish<CacheMessage> cacheMessagePublish(RedisTemplate redisTemplate){
        return redisTemplate::convertAndSend;
    }

    /**
     * 注册一个基于Redis的缓存消息处理器,可以替换为其他消息中间件来实现相同的功能
     *
     * @return RedisMessageListenerContainer
     */
    @Bean
    @ConditionalOnMissingBean(name="cacheMessageListenerContainer")
    @ConditionalOnClass(RedisConnectionFactory.class)
    RedisMessageListenerContainer cacheMessageListenerContainer(RedisConnectionFactory redisConnectionFactory,
                                                                Optional<Collection<MessageListenerAdapter>> listenerAdapters,
                                                                XCacheProperties xCacheProperties) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();
        redisMessageListenerContainer.setConnectionFactory(redisConnectionFactory);
        listenerAdapters.orElse(new ArrayList<>()).forEach(listenerAdapter ->{
            if(listenerAdapter.getDelegate() instanceof BinaryCacheMessageConsume){
                redisMessageListenerContainer.addMessageListener(listenerAdapter, new ChannelTopic(xCacheProperties.getCacheMessageTopic()));
            }
        } );
        return redisMessageListenerContainer;
    }

    @Bean
    @ConditionalOnClass(RedissonClient.class)
    CacheDequeFactory cacheDequeFactory(RedissonClient redissonClient,XCacheProperties xCacheProperties){
        return new RedissonCacheDequeFactory(redissonClient, xCacheProperties);
    }

    @Bean
    @ConditionalOnClass(RedisTemplate.class)
    RedisCacheFactory redisCacheFactory(XCacheProperties xCacheProperties,RedisTemplate<Object,Object> redisTemplate){
        return new RedisCacheFactory(xCacheProperties,redisTemplate);
    }

}