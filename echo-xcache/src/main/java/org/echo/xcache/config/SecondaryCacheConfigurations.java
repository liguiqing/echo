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
import org.echo.messaging.MessagePublish;
import org.echo.xcache.CacheFactory;
import org.echo.xcache.XCacheProperties;
import org.echo.xcache.binary.BinaryCacheManager;
import org.echo.xcache.binary.BinaryCacheMessageConsume;
import org.echo.xcache.caffeine.CaffeineCacheFactory;
import org.echo.xcache.caffeine.CaffeineCacheProperties;
import org.echo.xcache.message.CacheMessage;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.util.Optional;

/**
 * @author Liguiqing
 * @since V3.0
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(
        value = {
                XCacheProperties.class,
                CaffeineCacheProperties.class
        })
public class SecondaryCacheConfigurations {


    @Bean("SecondaryCacheManager")
    static BinaryCacheManager cacheManager(Optional<CacheFactory> secondaryCacheFactory,
                                           MessagePublish<CacheMessage> messagePublish,
                                           XCacheProperties xCacheProperties,
                                           CaffeineCacheProperties caffeineCacheProperties) {
        CaffeineCacheFactory caffeineCacheFactory = new CaffeineCacheFactory(caffeineCacheProperties);
        return  new BinaryCacheManager(
                xCacheProperties,
                caffeineCacheFactory,
                secondaryCacheFactory.orElse(caffeineCacheFactory),
                messagePublish);
    }

    @Bean
    MessageListenerAdapter cacheMessageListenerAdapter(BinaryCacheManager cacheManager) {
        return new MessageListenerAdapter(new BinaryCacheMessageConsume(cacheManager), "consume");
    }

}