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

package org.echo.redis.lock;

import com.google.common.collect.Lists;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * @author Liguiqing
 * @since V3.0
 */
@NoArgsConstructor
@Getter
@EqualsAndHashCode(of = {"id"})
@ToString(of = {"id","name"})
public class RedisLockTestBean implements Serializable {
    private Long id;

    private String name;

    private Double score;

    private LocalDateTime fetchTime;

    private LocalDateTime submitTime;

    private String maker;

    private ArrayList<String> makers = Lists.newArrayList();

    public RedisLockTestBean(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public void fetch(){
        this.fetchTime = LocalDateTime.now();
    }

    public void scored(String maker,Double score){
        this.maker = maker;
        if(this.score != null)
            this.score += score;
        else
            this.score = score;

        this.submitTime = LocalDateTime.now();
        this.makers.add(maker);
    }

    public boolean isUndo(){
        return this.maker == null;
    }
}