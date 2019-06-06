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


import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.function.*;

/**
 * <p>
 * 需要通过中间值进行计算的指标
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-04 07:57
 * @since V1.0.0
 **/
@EqualsAndHashCode(of = {"title"})
public class BiIndex<A, T, R extends Serializable> implements Index<T, R> {
    private String title;

    private R value;

    private boolean finished = false;

    private transient Predicate<T> condition;

    private transient Supplier<A> supplier;

    private transient BiConsumer<A, T> accumulator;

    private transient Function<A, R> finisher;

    private transient A container;

    private transient BinaryOperator<A> combiner;

    protected BiIndex(){}

    public BiIndex(String title, Supplier<A> supplier) {
        this(title, supplier,(a1,a2)->a1);
    }

    public BiIndex(String title, Supplier<A> supplier, BinaryOperator<A> combiner) {
        this(title, supplier, (t, u) -> {},combiner);
    }

    public BiIndex(String title, Supplier<A> supplier, BiConsumer<A, T> accumulator, BinaryOperator<A> combiner) {
        this(title, t -> true, supplier, accumulator,combiner);
    }

    public BiIndex(String title, Supplier<A> supplier, BiConsumer<A, T> accumulator,
                   BinaryOperator<A> combiner,
                   Function<A, R> finisher) {
        this(title, t -> true, supplier, accumulator,combiner,finisher);
    }

    public BiIndex(String title, Predicate<T> condition, Supplier<A> supplier, BiConsumer<A, T> accumulator,
                   BinaryOperator<A> combiner) {
        this(title, condition, supplier, accumulator, combiner,i -> (R) i);
    }

    public BiIndex(String title, Predicate<T> condition,
                   Supplier<A> supplier,
                   BiConsumer<A, T> accumulator,
                   BinaryOperator<A> combiner,
                   Function<A, R> finisher) {
        this.title = title;
        this.condition = condition;
        this.supplier = supplier;
        this.container = supplier.get();
        this.accumulator = accumulator;
        this.finisher = finisher;
        this.combiner = combiner;
    }

    @Override
    public String getTitle() {
        return this.title;
    }

    @Override
    public R getValue() {
        if(!this.finished){
            this.value = this.finisher.apply(this.container);
            this.finished = true;
        }
        return this.value;
    }

    @Override
    public void combine(Index other) {
        if(other instanceof BiIndex){
            BiIndex index = (BiIndex) other;
            this.container = this.combiner.apply(this.container,(A)index.container);
            this.finished = false;
        }
    }

    @Override
    public void cal(T t) {
        if (this.condition.test(t)) {
            this.accumulator.accept(this.container, t);
        }
    }

    @Override
    public BiIndex<A, T, R> deepClone(){
        return new BiIndex(this.title,this.condition,this.supplier,this.accumulator,this.combiner,this.finisher);
    }
}
