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

package org.echo.data;


import com.google.common.collect.Lists;
import org.echo.data.collection.IterableDataSet;
import org.echo.data.index.IndexGroup;
import org.echo.data.index.Indexes;
import org.echo.data.load.JdbcLazyDequeDataLoader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * <p>
 * DataIntegration Test
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-03 15:28
 * @since V1.0.0
 **/
@DisplayName("DataIntegration Test")
class DataIntegrationTest {

    @Test
    void test()throws Exception{
        JdbcOperations jdbc = mock(JdbcOperations.class);
        when(jdbc.queryForObject(any(String.class),any(RowMapper.class), any(Object[].class))).thenReturn(11);
        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getLong(any(String.class))).thenReturn(1L);
        List<IndexTestBean> list1 =  IndexTestBean.buildData(1, 3);
        List<IndexTestBean> list2 =  IndexTestBean.buildData(3, 5);
        List<IndexTestBean> list3 =  IndexTestBean.buildData(5, 7);
        List<IndexTestBean> list4 =  IndexTestBean.buildData(7, 9);
        List<IndexTestBean> list5 =  IndexTestBean.buildData(9, 11);
        List<IndexTestBean> list6 =  IndexTestBean.buildData(11, 12);

        List<IndexTestBean> all = Lists.newArrayList();
        all.addAll(list1);
        all.addAll(list2);
        all.addAll(list3);
        all.addAll(list4);
        all.addAll(list5);
        all.addAll(list6);

        double sum = all.stream().mapToDouble(t->t.getS()).sum();
        int total = all.size();
        double avg = all.stream().mapToDouble(t->t.getS()).average().getAsDouble();

        when(jdbc.query(any(String.class), any(RowMapper.class), any(Object.class))).thenReturn(list1,list2,list3,list4,list5,list6).thenReturn(null);
        JdbcLazyDequeDataLoader<IndexTestBean> dataLoader = new JdbcLazyDequeDataLoader<>(jdbc,(r) -> list1.get(0),"select 1 as value from dual");
        IndexGroup<IndexTestBean> ig = new IndexGroup<>("T1");

        ig.append(Indexes.total("Total"));
        ig.append(Indexes.sumOfDouble("Sum",(IndexTestBean t)->t.getS()));
        ig.append(Indexes.average("Avg",(IndexTestBean t)->t.getS()));

        IterableDataSet<IndexTestBean> dataSet = new IterableDataSet<>(dataLoader);
        IndexGroup<IndexTestBean> nig = dataSet.stream().parallel().collect(ig.toCollector());
        nig.getIndexes().forEach(i->{
            switch (i.getTitle()){
                case "Avg":
                    assertEquals(avg,i.getValue());
                    break;
                case "Sum":
                    assertEquals(sum,i.getValue());
                    break;
                case "Total":
                    assertEquals(total,i.getValue());
            }
        });
    }
}
