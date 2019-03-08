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

package org.echo.xcache.caffeine;

import lombok.*;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Liguiqing
 * @since V1.0
 */
@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@EqualsAndHashCode(of={"name"})
public class CaffeineProperties {

    /** 缓存名称 */
    private String name;

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 访问后过期时间，单位毫秒*/
    private long expireAfterAccess = 36000;

    /** 写入后过期时间，单位毫秒*/
    private long expireAfterWrite = 36000;

    /** 写入后刷新时间，单位毫秒*/
    private long refreshAfterWrite = 36000;

    /** 初始化大小*/
    private int initialCapacity = 500;

    /** 最大缓存对象个数，超过此数量时之前放入的缓存将失效*/
    private long maximumSize =5000;
}