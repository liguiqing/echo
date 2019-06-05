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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

@DisplayName("DataLoaderReentrantDistributedLock Test")
class DataLoaderReentrantDistributedLockTest {

    @Test
    void lock() {
        DataLoader dataLoader = mock(DataLoader.class);
        DataLoaderReentrantDistributedLock<String,DataLoader> lock = new DataLoaderReentrantDistributedLock<>();
        lock.lock("A",dataLoader,(i)->i.hasNext());
        lock.lock("A",dataLoader,(i)->{throw new NullPointerException();});
    }
}