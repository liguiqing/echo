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
/**
 * <pre>
 * 二元缓存
 * 配置参数（yml）
 * echo:
 *   xcache:
 *     cacheNullValues: false
 *     defaultExpiration: 360000
 *     #缓存默认时间
 *     expires: {cache1: 6000,cache2: 5000}
 *     cachePrefix: echo
 *     level2Enabled: true
 *     cacheMessageTopic: echo:binaryCache:topic
 *     #caffeine
 *     caffeine:
 *       # 默认一级缓存配置
 *       defaultProp:
 *         cacheNullValues: false
 *         expireAfterAccess: 600
 *         expireAfterWrite: 600
 *         refreshAfterWrite: 1
 *         initialCapacity: 50
 *         maximumSize: 50
 *       #启动时加载的缓存
 *       cachesOnBoot:
 *         - name: cache1
 *           cacheNullValues: false
 *           expireAfterAccess: 600
 *           expireAfterWrite: 600
 *           refreshAfterWrite: 1
 *           initialCapacity: 50
 *           maximumSize: 50
 *         - name: cache2
 *           cacheNullValues: false
 *           expireAfterAccess: 6000
 *           expireAfterWrite: 6000
 *           refreshAfterWrite: 100
 *           initialCapacity: 500
 *           maximumSize: 500
 * </pre>
 */
package org.echo.xcache;