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

package org.echo.xcache.binary;

import org.echo.xcache.message.CacheMessage;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class BinaryCacheMessageConsumeTest {

    @Test
    void consume() {
        BinaryCacheManager cacheManager = mock(BinaryCacheManager.class);
        BinaryCacheMessageConsume cacheMessageConsume = new BinaryCacheMessageConsume(cacheManager);
        CacheMessage m1 = new CacheMessage("A","A","AA");
        CacheMessage m2 = new CacheMessage("A1","A1","AB",2);
        cacheMessageConsume.consume(m1);
        cacheMessageConsume.consume(m2);
    }
}