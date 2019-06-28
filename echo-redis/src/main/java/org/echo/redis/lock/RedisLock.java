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

package org.echo.redis.lock;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisStringCommands;
import org.springframework.data.redis.connection.ReturnType;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.types.Expiration;

import java.nio.charset.Charset;

/**
 * Redis分成式事务锁
 *
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
public class RedisLock {
    private static final String CHARSET_NAME = "UTF-8";

    private static final byte[] UNLOCK_SCRIPT = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end".getBytes(Charset.forName(CHARSET_NAME));

    private static final String LOCK_KEY = "lock:";


    private String lockPrefix;

    private RedisOperations redisOperations;

    /**
     * 尝试获取分布式锁
     * @param key 锁
     * @param requestId 请求标识
     * @param expireTime 超期时间
     * @return 是否获取成功
     */
    public boolean lock(String key, String requestId, int expireTime){
        log.debug("Lock redis key {}->{}",requestId,key);

        String lockKey = lockPrefix + ":" + LOCK_KEY + key;
        RedisCallback<Boolean> callback = connection ->
             connection.set(lockKey.getBytes(Charset.forName(CHARSET_NAME)),
                    requestId.getBytes(Charset.forName(CHARSET_NAME)),
                    Expiration.seconds(expireTime), RedisStringCommands.SetOption.SET_IF_ABSENT);

        return (Boolean)redisOperations.execute(callback);
    }

    /**
     * 释放分布式锁
     * @param key 待释放的key
     * @param requestId 请求标识
     * @return 是否释放成功
     */
    public boolean unlock(String key,String requestId) {
        log.debug("Unlock redis key {}->{}", requestId, key);

        String lockKey = lockPrefix + ":" + LOCK_KEY + key;
        RedisCallback<Boolean> callback = connection ->
                connection.eval(UNLOCK_SCRIPT, ReturnType.BOOLEAN, 1, lockKey.getBytes(Charset.forName(CHARSET_NAME)),
                        requestId.getBytes(Charset.forName(CHARSET_NAME)));

        return (Boolean) redisOperations.execute(callback);
    }
}