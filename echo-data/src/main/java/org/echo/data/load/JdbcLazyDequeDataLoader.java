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


import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.echo.lock.DistributedLock;
import org.echo.util.CollectionsUtil;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * <pre>
 * 基于Jdbc和Deque的并支持延迟数据加载器.
 * 即一次只加载指定数据量的数据，当数据消耗到满足loaderStrategy时，再加载指定数据量的数据.依次，直到数据加载完毕
 * 数据集合特性同于{@link Deque}
 * 如果使用前不调用{@link #setResult(Deque)},将使用{@link LinkedBlockingDeque}作为数据存储容器
 * 如果在使用过程中调用{@link #setResult(Deque)},将重新进行初始化，数据从新加载
 * </pre>
 *
 * @author liguiqing
 * @date 2019-05-31 21:12
 * @since V1.0.0
 * @param <T> the type of elements held in this JdbcLazyDequeDataLoader
 **/
@Slf4j
public class JdbcLazyDequeDataLoader<T> implements DataLoader<T> {
    private static final String LOCK_KEY = UUID.randomUUID().toString();

    private JdbcOperations jdbc;

    private int size;

    private int popTotal;

    private int first;

    private int maxCapacity ;

    private Function<? super ResultSet,T> extractor;

    private String sql;

    private Object[] sqlArgs;

    private Deque<T> result;

    @Setter
    private DistributedLock<String, JdbcLazyDequeDataLoader> lock;

    //数据加载策略
    @Setter
    private Predicate<Collection<T>> loaderStrategy ;

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, Function<? super ResultSet, T> extractor, String sql) {
        this(jdbc, 1000, extractor, sql);
    }

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, int maxCapacity, Function<? super ResultSet, T> extractor, String sql) {
        this(jdbc, maxCapacity, extractor, sql, Optional.empty());
    }

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, int maxCapacity, Function<? super ResultSet, T> extractor, String sql, Optional<Object[]> args) {
        this.jdbc = jdbc;
        this.maxCapacity = maxCapacity;
        this.extractor = extractor;
        this.sql = sql;
        this.lock = new DataLoaderReentrantDistributedLock();
        this.loaderStrategy = c-> CollectionsUtil.isNullOrEmpty(result);
        this.extendArgs(args.orElse(new Object[]{}));
        this.ensureSize(args.orElse(new Object[]{}));
    }

    @Override
    public void load() {
        this.lock.lock(LOCK_KEY + "_load",this,self->{
            if(Objects.isNull(this.result))
                self.setResult(new LinkedBlockingDeque<>());

            self.loadData();
        });
    }

    @Override
    public boolean hasNext() {
        return this.popTotal < this.size;
    }

    @Override
    public T next() {
        loadMore();

        if(this.result.isEmpty())
            throw new NoSuchElementException();

        T e = this.result.pop();
        this.popTotal ++;
        return e;
    }

    @Override
    public long size() {
        return this.size;
    }

    private void ensureSize(Object[] args){
        String countSql = String.format("select count(1) ct from (%s) a",this.sql);
        log.debug("Get size of {} with {}",countSql,Arrays.deepToString(args));
        this.size = jdbc.queryForObject(countSql, (r,i)->r.getInt("ct"),args);
    }

    public void setResult(Deque<T> result){
        Objects.requireNonNull(result, "DataLoader result not null!");
        if(!Objects.isNull(this.result)){
            this.first = 0;
            this.popTotal = 0;
            this.result.clear();
        }
        this.result = result;
        this.loadData();
    }

    /**
     * 剩余数量
     * @return number of left
     */
    public int left(){
        return this.size - this.popTotal;
    }

    private void extendArgs(Object[] args){
        var allArgs = Stream.of(args).collect(Collectors.toList());
        allArgs.add(this.first);
        allArgs.add(this.maxCapacity);
        this.sqlArgs = allArgs.toArray();
    }

    private void loadData(){
        var list = this.jdbc.query(this.sql.concat(" limit ?,? "),(r, i)->this.extractor.apply(r), this.sqlArgs);
        if(CollectionsUtil.isNullOrEmpty(list)) return;
        log.debug("Load data {}",list.size());
        this.first += list.size();
        this.result.addAll(list);
    }

    private void loadMore(){
        if(this.loaderStrategy.test(this.result)){
            this.lock.lock(LOCK_KEY,this, JdbcLazyDequeDataLoader::loadData);
        }
    }
}