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


import java.util.Iterator;

/**
 * <p>
 * 数据加载器,特性请参考{@link Iterator}
 * </P>
 *　
 * @author liguiqing
 * @date 2019-05-31 20:52
 * @since V1.0.0
 * @param <T> the type of elements held in this DataLoader
 **/
public interface DataLoader<T> extends Iterator<T> {

    /**
     * load elements of E
     * 每调用一次数据都从头开始加载，所有的数据都会被清理掉
     */
    void load();

    /**
     *
     * @return  of total of elements
     */
    long size();
}
