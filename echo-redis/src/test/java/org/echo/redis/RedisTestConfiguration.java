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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;

import java.io.Serializable;

/**
 * A mock redis configuration
 *
 * @author liguiqing
 * @since V1.0.0 2019-06-14 13:49
 **/
@Configuration
public class RedisTestConfiguration {

    @Bean(name = "redisMessagePublish")
    static  MessagePublish<String> redisMessagePublish(RedisTemplate redisTemplate){
        return redisTemplate::convertAndSend;
    }


    @Bean
    RedisMessageListenerContainer container(RedisConnectionFactory connectionFactory,
                                            MessageListenerAdapter listenerAdapter) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(listenerAdapter, new PatternTopic("echo:test:topic"));
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter() {
        var consume = new MessageConsume<String>(){

            private int count = 0;

            @Override
            public void consume(String message) {
                count++;
            }

            public int getCount(){
                return count;
            }
        };
        return new MessageListenerAdapter(consume, "consume");
    }

}
