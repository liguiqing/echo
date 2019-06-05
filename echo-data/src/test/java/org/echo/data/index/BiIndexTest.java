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
import org.echo.data.KeyGen;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

@DisplayName("BiIndex Test")
class BiIndexTest {

    @RepeatedTest(10)
    void test(){
        var r1 = new BiIndex<Integer, IndexTestBean,Integer>("T1",()->0);

        var rs = IndexTestBean.buildData(1, 11,Stream.of(new KeyGen(3, "S2"), new KeyGen(1, "S1")).collect(Collectors.toList()));
        var sumS = rs.stream().mapToDouble(r->r.getS()).sum();
        rs.forEach(rb-> r1.cal(rb));
        assertTrue(r1.getValue().intValue() == 0);
        var r2 = new BiIndex<double[], IndexTestBean,Double>("T1",()->new double[1],(t, u)->t[0]+=u.getS(),(t1, t2)->t1,(t)->t[0]);
        rs.forEach(rb-> r2.cal(rb));
        assertEquals(sumS,r2.getValue().doubleValue());


        var r3 = Indexes.average("T1",(IndexTestBean t)->t.getS());
        rs.forEach(rb-> r3.cal(rb));
        var avg = rs.stream().mapToDouble(r->r.getS()).average();
        assertEquals(avg.getAsDouble(),r3.getValue().doubleValue());

        Index total1 = Indexes.total("T");
        rs.forEach(rb->total1.cal(rb));
        assertEquals(10,total1.getValue());

        Index total2 = Indexes.total("T",(IndexTestBean t)->t.getId().equals("S2"));
        rs.forEach(rb->total2.cal(rb));
        assertEquals(3,total2.getValue());
        assertEquals("T",total2.getTitle());
        total1.combine(total2);
        assertEquals(13,total1.getValue());
        var total3 = total1.deepClone();
        rs.forEach(rb->total3.cal(rb));
        assertEquals(10,total3.getValue());
        assertEquals("T",total3.getTitle());

        var sum1 = Indexes.sumOfDouble("Sum",(IndexTestBean t)->t.getS());
        rs.forEach(rb->sum1.cal(rb));
        assertEquals(sumS,sum1.getValue().doubleValue());
        sum1.combine(r2);
        assertEquals(sumS * 2,sum1.getValue().doubleValue());
        sum1.combine(null);
        sum1.combine(mock(Index.class));
        assertEquals(sumS * 2,sum1.getValue().doubleValue());
    }

}