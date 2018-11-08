/**
 * Redis+CaffeineProperties
 * Spring 本地一级缓存+远程二级缓存组件
 *
 * 使用Redis作为二级缓存, Caffeine作为一级缓存
 * Spring Boot 自动配置类: RedisCaffeineCacheAutoConfiguration
 *             配置参数(yml):
 *             spring:
 *                  cache:
 *                    redis-caffeine:
 *                      caffeine:
 *                        expireAfterAccess: 5000
 *                        level2Enabled: false
 *                      redis:
 *                        defaultExpiration: 60000
 * 注:设计上应该抽象为一组接口,将缓存,消息处理组伯
 * Copyright (c) 2016,2018, 深圳市易考试乐学测评有限公司
 **/
package org.echo.spring.cache.redis;