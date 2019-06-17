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
import org.echo.exception.ThrowableToString;
import org.echo.lock.DistributedLock;
import org.redisson.api.RedissonClient;

import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * An implements fo DistributedLock based Redisson
 *
 * @author Liguiqing
 * @since V1.0
 */
@AllArgsConstructor
@Slf4j
public class RedissonDistributedLock<K,V> implements DistributedLock<K,V> {

    private RedissonClient redissonClient;

    @Override
    public V lock(K key, Callable<V> call){
        String myKey = String.valueOf(key);
        boolean locked = redissonClient.getFairLock(myKey).tryLock();

        try{
            if(locked) return call.call();
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }finally {
            redissonClient.getFairLock(myKey).unlock();
        }
        return null;
    }

    @Override
    public void lock(K key, V v, Consumer<V> consumer) {
        String myKey = String.valueOf(key);
        boolean locked = redissonClient.getFairLock(myKey).tryLock();
        try{
            if(locked)  consumer.accept(v);
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }finally {
            redissonClient.getFairLock(myKey).unlock();
        }
    }
}