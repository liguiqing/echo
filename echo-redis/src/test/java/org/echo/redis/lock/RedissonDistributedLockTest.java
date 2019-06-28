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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Echo : RedissonDistributedLock test")
class RedissonDistributedLockTest {

    @Test
    void lock(){
        RedissonClient redissonClient = mock(RedissonClient.class);
        RLock lock = mock(RLock.class);
        when(lock.tryLock()).thenReturn(true).thenReturn(false).thenReturn(true).thenReturn(true).thenReturn(false).thenReturn(true);
        when(redissonClient.getFairLock(anyString())).thenReturn(lock);
        RedissonDistributedLock<String,Integer> distributedLock = new RedissonDistributedLock(redissonClient);

        assertEquals(Integer.valueOf(1),distributedLock.lock("aa",()->1));
        assertNull(distributedLock.lock("aa",()->1));

        distributedLock.lock("aa",1,s -> assertTrue(1 == s));
        distributedLock.lock("aa",1,s -> assertTrue(1 == s));
        //distributedLock.lock("aa",0,s -> assertEquals(0,s));
        distributedLock.lock("aa",0,s -> {final int a = s;assertThrows(Exception.class,()->{var b= a/1;});});
    }
}