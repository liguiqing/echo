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

package org.echo.redis;


import org.echo.messaging.MessageConsume;
import org.echo.messaging.MessagePublish;
import org.echo.redis.config.RedisBaseComponentConfiguration;
import org.echo.redis.lock.RedisLock;
import org.echo.util.ClassUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.redisson.api.RTopic;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.Resource;
import java.util.Set;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 * Redis Configurations Test
 *
 * @author liguiqing
 * @since V1.0.0 2019-06-12 13:28
 **/
@DisplayName("Redis Configuration Test")
@ExtendWith(SpringExtension.class)
@ContextConfiguration( initializers = {ConfigFileApplicationContextInitializer.class},
        classes = {RedisAutoConfiguration.class, RedissonAutoConfiguration.class,
                RedisBaseComponentConfiguration.class,RedisTestConfiguration.class})
class ConfigurationsTest {

    @Autowired
    private ApplicationContext ctx;

    @Resource(name = "redisTemplate")
    private RedisTemplate template;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private MessagePublish redisMessagePublish;

    @Autowired
    private RedisConnectionFactory connectionFactory;

    @Autowired
    private RedisMessageListenerContainer container;

    @Autowired
    private MessageListenerAdapter adapter;

    @Autowired
    private RedisLock redisLock;

    @Test
    void test(){
        template.opsForValue().set("echo:test:a","Hahaha");
        Set<String> keys = template.keys("echo:test*");
        assertEquals(1, keys.size());
        template.delete("echo:test:a");
        assertNull(template.opsForValue().get("echo:test:a"));

        redisMessagePublish.publish("echo:test:topic","aa");
        redisMessagePublish.publish("echo:test:topic1","ab");
        redisMessagePublish.publish("echo:test:topic","ab");

        MessageConsume consume = (MessageConsume)adapter.getDelegate();
        await().atMost(2,SECONDS).until(() -> (Integer)ClassUtils.invoke(consume,"getCount"),equalTo(2));

        RTopic topic = redissonClient.getTopic("echo:test:message");
        redisLock.lock("a", "a", 5);
        redisLock.unlock("a", "a");
    }
}
