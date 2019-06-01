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

package org.echo.data.collection;

import org.echo.data.load.DataLoader;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IterableDataSetTest {

    @Test
    void test(){
        DataLoader<Integer> dataLoader1 = mock(DataLoader.class);
        when(dataLoader1.hasNext()).thenReturn(true,true,true,true,true,true,true,true,true,true,true,false);
        when(dataLoader1.size()).thenReturn(10l);
        when(dataLoader1.next()).thenReturn(1,2,3,4,5,6,7,8,9,10,null);
        IterableDataSet<Integer> dataSet1 = new IterableDataSet(dataLoader1);
        assertTrue(true);
        assertEquals(10,dataSet1.size());
        assertTrue(dataSet1.hasNext());
        Supplier<Stream<Integer>> ds = ()->dataSet1.stream();
        int sum = ds.get().mapToInt((i)->i).sum();
        assertEquals(Stream.of(1,2,3,4,5,6,7,8,9,10).mapToInt(i->i).sum(),sum);
    }
}