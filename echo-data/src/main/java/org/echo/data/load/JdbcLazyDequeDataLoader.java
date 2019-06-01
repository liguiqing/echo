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
import org.echo.exception.ThrowableToString;
import org.echo.lock.DistributedLock;
import org.echo.util.CollectionsUtil;
import org.springframework.jdbc.core.JdbcOperations;

import java.sql.ResultSet;
import java.util.*;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * 基于Jdbc和Deque的并支持延迟数据加载器.
 * 即一次只加载指定数据量的数据，当数据消耗到符合loaderStrategy时，再加载其他数据.依次，直到数据加载完毕　
 * 数据集合特性同于{@link Deque}
 * </P>
 *
 * @author liguiqing
 * @date 2019-05-31 21:12
 * @since V1.0.0
 * @param <E> the type of elements held in this JdbcLazyDequeDataLoader
 **/
@Slf4j
public class JdbcLazyDequeDataLoader<E> implements DataLoader<E> {
    private static final String LOCK_KEY = UUID.randomUUID().toString();

    private JdbcOperations jdbc;

    private int size;

    private int popTotal;

    private int first;

    private int maxCapacity ;

    private Function<? super ResultSet,E> extractor;

    private String sql;

    private Object[] sqlArgs;

    private Deque<E> result;

    @Setter
    private DistributedLock<String, JdbcLazyDequeDataLoader> lock;

    //数据加载策略
    @Setter
    private Predicate<Collection<E>> loaderStrategy;

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, Function<? super ResultSet, E> extractor, String sql) {
        this(jdbc, 1000, extractor, sql);
    }

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, int maxCapacity, Function<? super ResultSet, E> extractor, String sql) {
        this(jdbc, maxCapacity, extractor, sql, Optional.empty());
    }

    public JdbcLazyDequeDataLoader(JdbcOperations jdbc, int maxCapacity, Function<? super ResultSet, E> extractor, String sql, Optional<Object[]> args) {
        this.jdbc = jdbc;
        this.maxCapacity = maxCapacity;
        this.extractor = extractor;
        this.sql = sql;
        this.sqlArgs = args.orElse(new Object[]{this.first,this.maxCapacity});
        this.lock = new DistributedLock<>(){

            private ReentrantLock lock = new ReentrantLock();

            @Override
            public void lock(String key, JdbcLazyDequeDataLoader jdbcDequeDataLoader, Consumer<JdbcLazyDequeDataLoader> consumer) {
                try{
                    if(lock.tryLock(2,TimeUnit.SECONDS)){
                        consumer.accept(jdbcDequeDataLoader);
                    }
                }catch (Exception e){
                    log.warn(ThrowableToString.toString(e));
                }finally {
                    lock.unlock();
                }
            }
        };
        this.loaderStrategy = c-> CollectionsUtil.isNullOrEmpty(result);
        this.ensureSize();

    }

    @Override
    public void load() {
        this.lock.lock(LOCK_KEY + "_load",this,self->{
            if(self.first == 0)
                return;

            if(Objects.isNull(this.result))
                self.result = new LinkedBlockingDeque<>();

            self.result.clear();
            self.loadData();
        });
    }

    @Override
    public boolean hasNext() {
        return this.popTotal < this.size;
    }

    @Override
    public E next() {
        if(this.loaderStrategy.test(this.result)){
            this.lock.lock(LOCK_KEY,this, JdbcLazyDequeDataLoader::loadData);
        }

        if(this.result.isEmpty())
            throw new NoSuchElementException();

        E e = this.result.pop();
        this.popTotal ++;
        return e;
    }

    @Override
    public long size() {
        return this.size;
    }

    private void ensureSize(){
        //TODO
        this.size = this.maxCapacity * 10 + 1;
    }

    public void setResult(Deque<E> result){
        this.first = 0;
        this.result.clear();
        this.result = result;
    }

    private void loadData(){
        var list = this.jdbc.query(this.sql,(r, i)->this.extractor.apply(r), this.sqlArgs);
        if(CollectionsUtil.isNullOrEmpty(list))
            return ;
        this.first += list.size();
        this.result.addAll(list);
    }
}
