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
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("JdbcLazyDequeDataLoader Test")
class JdbcLazyDequeDataLoaderTest {

    @Test
    void test()throws Exception{
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(jdbc.queryForObject(any(String.class),any(RowMapper.class), any(Object[].class))).thenReturn(11);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong(any(String.class))).thenReturn(1L);
        List<LoaderTestBean> list1 = Stream.of(new LoaderTestBean("name", 1L, LocalDate.now()),new LoaderTestBean("name", 2L, LocalDate.now())).collect(Collectors.toList());
        List<LoaderTestBean> list2 = Stream.of(new LoaderTestBean("name", 3L, LocalDate.now()),new LoaderTestBean("name", 4L, LocalDate.now())).collect(Collectors.toList());
        List<LoaderTestBean> list3 = Stream.of(new LoaderTestBean("name", 5L, LocalDate.now()),new LoaderTestBean("name", 6L, LocalDate.now())).collect(Collectors.toList());
        List<LoaderTestBean> list4 = Stream.of(new LoaderTestBean("name", 7L, LocalDate.now()),new LoaderTestBean("name", 8L, LocalDate.now())).collect(Collectors.toList());
        List<LoaderTestBean> list5 = Stream.of(new LoaderTestBean("name", 9L, LocalDate.now()),new LoaderTestBean("name", 10L, LocalDate.now())).collect(Collectors.toList());
        List<LoaderTestBean> list6 = Stream.of(new LoaderTestBean("name", 11L, LocalDate.now())).collect(Collectors.toList());
        when(jdbc.query(any(String.class), any(RowMapper.class), any(Object.class))).thenReturn(list1,list2,list3,list4,list5,list6).thenReturn(null);
        Function<ResultSet, LoaderTestBean> extractor = (r) -> new LoaderTestBean("name" , 1L, LocalDate.now());
        JdbcLazyDequeDataLoader<LoaderTestBean> dataLoader = new JdbcLazyDequeDataLoader<>(jdbc,extractor,"select 1 as value from dual");

        assertEquals(11,dataLoader.size());
        dataLoader.load();
        assertEquals(11,dataLoader.left());
        for(var i =1l;i<=11;i++){
            LoaderTestBean t = dataLoader.next();
            assertTrue(t.getVale().compareTo(i) == 0);
            assertEquals(dataLoader.size() - i,dataLoader.left());
            if(i <10)  assertTrue(dataLoader.hasNext());

        }
        assertFalse(dataLoader.hasNext());
        assertThrows(NoSuchElementException.class, () -> dataLoader.next());
        assertThrows(NoSuchElementException.class, () -> dataLoader.next());

        dataLoader.setResult(new LinkedBlockingDeque<>());
        assertTrue(dataLoader.hasNext());
        assertEquals(dataLoader.size(),dataLoader.left());

        assertThrows(NullPointerException.class,()-> dataLoader.setResult(null), "DataLoader result not null!");
        dataLoader.load();
    }
}