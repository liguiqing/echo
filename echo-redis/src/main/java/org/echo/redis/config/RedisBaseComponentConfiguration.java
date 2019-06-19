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

package org.echo.redis.config;


import org.echo.redis.lock.RedisLock;
import org.echo.redis.lock.RedissonDistributedLock;
import org.redisson.api.RedissonClient;
import org.redisson.spring.starter.RedissonAutoConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * <pre>
 * 基于redis实现的通用组件,配置依赖于{@link RedisAutoConfiguration} 及　{@link RedissonAutoConfiguration}
 * 提供以下组件：
 * 1. a distributed lock {@link RedisLock}
 * 2. a DistributedLock implements of Redisson
 * </Pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-06-15 14:30
 **/
@Configuration
@AutoConfigureAfter({RedisAutoConfiguration.class, RedissonAutoConfiguration.class})
public class RedisBaseComponentConfiguration {

    @Bean
    static RedisLock redisLock(@Value("${echo.redis.lock.prefix:echo:lock:}") String prefix, RedisTemplate redisTemplate){
        return new RedisLock(prefix , redisTemplate);
    }

    @Bean
    static RedissonDistributedLock redissonDistributedLock(RedissonClient redissonClient){
        return new RedissonDistributedLock(redissonClient);
    }

    @Bean
    static RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory)  {
        RedisTemplate<Object, Object> template = new RedisTemplate<>();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }

    @Bean
    static StringRedisTemplate stringRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        StringRedisTemplate template = new StringRedisTemplate();
        template.setKeySerializer(new StringRedisSerializer());
        template.setConnectionFactory(redisConnectionFactory);
        return template;
    }
}
