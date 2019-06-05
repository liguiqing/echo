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

package org.echo.data.load;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.lock.DistributedLock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;

/**
 * <p>
 * 服务于DataLoader的可重入锁
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-03 07:36
 * @since V1.0.0
 **/
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class DataLoaderReentrantDistributedLock<K extends String,T extends DataLoader> implements DistributedLock<K,T> {

    private ReentrantLock lock = new ReentrantLock();

    private  long timeout = 2;

    @Override
    public void lock(K key, T t, Consumer<T> consumer) {
        try{
            if(lock.tryLock(timeout, TimeUnit.SECONDS)){
                consumer.accept(t);
            }
        }catch (Exception e){
            log.warn(ThrowableToString.toString(e));
        }finally {
            lock.unlock();
        }
    }
}
