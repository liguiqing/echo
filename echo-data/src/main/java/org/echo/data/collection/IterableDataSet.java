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

package org.echo.data.collection;


import lombok.AllArgsConstructor;
import org.echo.data.load.DataLoader;

import java.util.Iterator;
import java.util.Spliterators;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/**
 * <p>
 * 可迭代数据集
 * </P>
 *
 * @author liguiqing
 * @date 2019-05-31 20:55
 * @since V1.0.0
 * @param <T> the type of elements held in this IterableDataSet
 **/
@AllArgsConstructor
public class IterableDataSet<T> implements DataSet<T>, Iterator<T> {

    private DataLoader<T> dataLoader;

    @Override
    public Stream<T> stream(){
        this.dataLoader.load();
        return StreamSupport.stream(Spliterators.spliterator(this,this.size(), 0), false);
    }

    @Override
    public boolean hasNext(){
        return dataLoader.hasNext();
    }

    @Override
    public T next() {
        return this.dataLoader.next();
    }

    @Override
    public long size() {
        return this.dataLoader.size();
    }
}
