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
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.commons.lang3.RandomUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * <p>
 *
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-03 13:53
 * @since V1.0.0
 **/
@AllArgsConstructor
@Getter
public class IndexTestBean {
    private String id;

    private double s;

    private LocalDateTime time1;

    private LocalDateTime time2;

    public long differenceBySecond(){
        return Duration.between(time2, time1).toSeconds();
    }

    public static List<IndexTestBean> buildData(int start, int end){
        return buildData(start, end, null);
    }

    public static List<IndexTestBean> buildData(int start, int end, List<KeyGen> keys) {
        List<IndexTestBean> list = Lists.newArrayList();
        IntStream.range(start, end).forEach(i ->{
            if(Objects.isNull(keys)){
                int k = RandomUtils.nextInt(5, 10);
                LocalDateTime t1 = LocalDateTime.now().minusSeconds(k);
                k = RandomUtils.nextInt(5, 10);
                LocalDateTime t2 = t1.plusSeconds(k);
                int s = RandomUtils.nextInt(0, 11);
                list.add(new IndexTestBean("K1", s * 1d, t1, t2));
            }else{
                for(KeyGen kg:keys) {
                    if (i % kg.index == 0) {
                        int k = RandomUtils.nextInt(5, 10);
                        LocalDateTime t1 = LocalDateTime.now().minusSeconds(k);
                        k = RandomUtils.nextInt(5, 10);
                        LocalDateTime t2 = t1.plusSeconds(k);
                        int s = RandomUtils.nextInt(0, 11);
                        list.add(new IndexTestBean(kg.key, s * 1d, t1, t2));
                        break;
                    }
                }
            }
        });
        return list;
    }
}
