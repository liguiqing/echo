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

package org.echo.lock;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("DistributedLock Test")
class DistributedLockTest {

    @Test
    void test()throws Exception{
        Callable c = mock(Callable.class);
        when(c.call()).thenReturn("aa").thenThrow(NullPointerException.class);
        DistributedLock<String,String> iLock = new DistributedLock(){};
        assertEquals("aa", iLock.lock("bb",c));
        iLock.lock("aa","AA",s -> assertEquals("AA",s));
        assertThrows(RuntimeException.class,()->iLock.lock("cc", c));

        assertThrows(LockFailureException.class,()->{throw new LockFailureException("aa");});
        assertThrows(LockFailureException.class,()->{throw new LockFailureException();});
    }
}