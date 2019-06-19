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

package org.echo.data.index;

import org.echo.data.IndexTestBean;
import org.echo.util.ClassUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Indexes Test")
class IndexesTest {

    @Test
    void total() {
        assertThrows(Exception.class, () -> ClassUtils.newInstanceOf(Indexes.class));

        Index total1 = Indexes.total("T1");
        assertEquals("T1", total1.getTitle());
        List<IndexTestBean> itbs = IndexTestBean.buildData(1, 11);
        itbs.forEach(t->total1.cal(t));
        assertEquals(itbs.size(),total1.getValue());
    }

    @Test
    void sumOfDouble() {
        Index i1 = Indexes.sumOfDouble("T1",(IndexTestBean t)->t.getS());
        assertEquals("T1", i1.getTitle());
        List<IndexTestBean> itbs = IndexTestBean.buildData(1, 11);
        double sum = itbs.stream().mapToDouble(t->t.getS()).sum();
        itbs.forEach(t->i1.cal(t));
        assertEquals(sum,i1.getValue());
    }

    @Test
    void average() {
        var i1 = Indexes.average("T1",(IndexTestBean t)->t.getS());
        assertEquals("T1", i1.getTitle());
        List<IndexTestBean> itbs = IndexTestBean.buildData(1, 11);
        var avg = itbs.stream().mapToDouble(t->t.getS()).average().getAsDouble();
        itbs.forEach(t->i1.cal(t));
        assertTrue(i1.getValue().doubleValue() == avg);
    }
}