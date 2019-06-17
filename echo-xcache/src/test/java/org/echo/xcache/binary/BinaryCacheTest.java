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

import org.echo.lock.DistributedLock;
import org.echo.messaging.MessagePublish;
import org.echo.xcache.NativeCaches;
import org.echo.xcache.XCacheProperties;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;

import java.util.concurrent.Callable;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("BinaryCache Test")
class BinaryCacheTest {

    @Test
    void test(){
        Cache l1 = mock(Cache.class);
        Cache l2 = mock(Cache.class);
        Cache.ValueWrapper value  = mock(Cache.ValueWrapper.class);
        when(value.get()).thenReturn("Test");
        when(l2.get("Test")).thenReturn(value).thenReturn(null);
        when(l1.get("Test")).thenReturn(null).thenReturn(value);

        MessagePublish pusher = mock(MessagePublish.class);
        XCacheProperties binaryCacheProperties = new XCacheProperties();
        binaryCacheProperties.setCacheNullValues(true);
        binaryCacheProperties.setLevel2Enabled(true);
        BinaryCache cache = new BinaryCache("Test",l1,l2, binaryCacheProperties,pusher);
        assertTrue(cache.isAllowNullValues());
        assertEquals("Test",cache.getName());
        assertNotNull(cache.getIdentifier());
        assertEquals(l1,cache.getNativeCache());

        assertEquals("Test",cache.get("Test").get());
        assertEquals("Test",cache.get("Test").get());

        assertEquals("Test1",cache.get("Test1",()->"Test1"));
        assertEquals("Test1",cache.get("Test1",()->"Test1"));
        //assertThrows(IllegalStateException.class,()->cache.get("Test1",()-> {throw new Exception();}));
        cache.evict("Test");
        cache.clearLocal("Test");
        cache.clearLocal(null);
        cache.clear();
        assertEquals(0, NativeCaches.size(cache));
        assertEquals(0,NativeCaches.keys(cache).size());
        assertEquals(0,NativeCaches.values(cache).size());

        cache.put("Test","exec");
        cache.putIfAbsent("Test","Test");
        cache.putIfAbsent("Test",null);

        binaryCacheProperties.setCacheNullValues(false);
        BinaryCache cache1 = new BinaryCache("Test",l1,l2, binaryCacheProperties,pusher);
        assertNull(cache1.putIfAbsent("Test",null));
        cache1.put("Test",null);

        Cache l11 = mock(Cache.class);
        Cache l22 = mock(Cache.class);
        when(l22.get("Test")).thenReturn(value);
        when(l11.get("Test")).thenReturn(value);
        BinaryCache cache2 = new BinaryCache("Cache2",l11,l22, binaryCacheProperties,pusher);
        cache2.close(0);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(1);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(2);
        assertNull(cache2.get("Test"));
        cache2.open(0);
        assertNull(cache2.get("Test"));
        cache2.open(2);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(2);
        assertNull(cache2.get("Test"));
        cache2.open(1);
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(1);
        assertNull(cache2.get("Test"));
        when(l22.get("Test")).thenReturn(value);
        when(l11.get("Test")).thenReturn(value).thenReturn(null);
        cache2.open(9);
        assertEquals(value.get(),cache2.get("Test").get());
        assertEquals(value.get(),cache2.get("Test").get());
        cache2.close(9);
        assertNull(cache2.get("Test"));
        assertNull(cache2.get("Test"));

        Cache only = mock(Cache.class);
        BinaryCache cache3 = BinaryCache.onlyCache1("Cache3", only, binaryCacheProperties, null);
        cache3.evict("Test");
        cache3.clear();

        cache3 = BinaryCache.onlyCache2("Cache3", only, binaryCacheProperties, null);
        cache3.evict("Test");
        cache3.clear();

        DistributedLock lock = mock(DistributedLock.class);
        when(lock.lock(any(Object.class), any(Callable.class))).thenThrow(new RuntimeException());
        Cache lockThrow = BinaryCache.onlyCache2("Cache3", only, binaryCacheProperties, lock);
        lockThrow.putIfAbsent("", "");
    }
}