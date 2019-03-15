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

import org.echo.lock.DistributedLock;
import org.echo.lock.RedisBaseDistributedLock;
import org.echo.util.RedisClientUtils;
import org.echo.xcache.CacheDequeFactory;
import org.echo.xcache.message.RedisCacheMessagePusher;
import org.echo.xcache.redis.RedisCacheFactory;
import org.echo.xcache.redis.RedisCacheProperties;
import org.echo.xcache.redis.RedissonCacheDequeFactory;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 二级缓存自动配置
 *
 * @author Liguiqing
 * @since V1.0
 */
@Configuration
@EnableConfigurationProperties(
    value = {
        RedisCacheProperties.class
    })
public class RedisCacheConfigurations {

    @Autowired
    private RedisCacheProperties redisCacheProperties;

    @Bean(destroyMethod="shutdown")
    public RedissonClient redisson(){
        org.redisson.config.Config config = new org.redisson.config.Config();
        config.useSingleServer().setAddress("redis://".concat(redisCacheProperties.getStandalone().toHost()));
        return Redisson.create(config);
    }

    @Bean
    public DistributedLock distributedLock(RedissonClient redissonClient, @Value("${app.util.lock.prefix:echo}")String lockPrefix){
        return new RedisBaseDistributedLock(lockPrefix, redissonClient);
    }

    @Bean
    public CacheDequeFactory cacheDequeFactory(RedissonClient redissonClient){
        if(RedisClientUtils.isAlive(redissonClient)){
            return new RedissonCacheDequeFactory(redissonClient, redisCacheProperties);
        }
        return new CacheDequeFactory(){};
    }

    @Bean
    public RedisTemplate<Object, Object> redisTemplate(JedisConnectionFactory connectionFactory){
        RedisTemplate<Object, Object> redisTemplate =  new RedisTemplate<>();
        StringRedisSerializer serializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(serializer);
        redisTemplate.setHashKeySerializer(serializer);
        redisTemplate.setConnectionFactory(connectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisClusterConfiguration redisClusterConfiguration(){
        return new RedisClusterConfiguration(redisCacheProperties.getHostsAndPorts());
    }

    @Bean
    public JedisConnectionFactory redisConnectionFactory(){
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(redisCacheProperties.getStandalone().getHost(),
                redisCacheProperties.getStandalone().getPort()));
    }

    @Bean
    public JedisPool jedisPool(){
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(1000);
        config.setMaxIdle(100);
        config.setMaxWaitMillis(10000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(true);
        config.setTimeBetweenEvictionRunsMillis(30000);
        config.setNumTestsPerEvictionRun(10);
        config.setMinEvictableIdleTimeMillis(60000);
        return new JedisPool(config,redisCacheProperties.getStandalone().getHost(),
                redisCacheProperties.getStandalone().getPort());
    }

    @Bean
    public RedisCacheFactory secondaryCacheFactory(RedisTemplate<Object,Object> redisTemplate,JedisPool jedisPool){
        return new RedisCacheFactory(this.redisCacheProperties,jedisPool,redisTemplate);
    }

    @Bean
    public RedisCacheMessagePusher redisCacheMessagePusher(RedisTemplate<Object, Object> redisTemplate){
        return new RedisCacheMessagePusher(redisTemplate);
    }
}