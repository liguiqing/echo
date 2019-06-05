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
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@DisplayName("IndexGroup Test")
class IndexGroupTest {

    @Test
    void test(){
        var i1 = Indexes.total("I1");
        var i2 = Indexes.sumOfDouble("I2",(IndexTestBean t)->t.getS());

        var g1 = new IndexGroup<IndexTestBean>("T1");
        var list = IndexTestBean.buildData(1, 6);
        int total = list.size();
        double sum = list.stream().mapToDouble(t->t.getS()).sum();

        assertNull(g1.getIndexes());
        assertNull(g1.getChildren());

        var g11 = new IndexGroup<IndexTestBean>("T11");
        var g12 = new IndexGroup<IndexTestBean>("T12");

        g1.append(i1).append(i2);
        g11.append(Indexes.total("I1")).append(Indexes.sumOfDouble("I2",(IndexTestBean t)->t.getS()));
        g12.append(Indexes.total("I1")).append(Indexes.sumOfDouble("I2",(IndexTestBean t)->t.getS()));
        g1.appendChild(g11).appendChild(g12).appendChild(g1);

        IndexGroup<IndexTestBean> ig = list.stream().parallel().collect(g1.toCollector());

        var l = 0;
        for(Index i:ig.getIndexes()){
            switch (l){
                case 1:
                    assertEquals(sum,i.getValue());
                    break;
                case 0:
                    assertEquals(total,i.getValue());
            }
            l++;
        }
    }
}