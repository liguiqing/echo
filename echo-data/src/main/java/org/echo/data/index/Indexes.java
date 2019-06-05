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


import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

/**
 * Implementations of {@link Index} that implement various useful reduction
 * operations, such as accumulating elements into Index, averaged
 * elements according to various criteria, etc.
 *
 *
 * @author liguiqing
 * @date 2019-06-04 12:23
 * @since V1.0.0
 **/
public class Indexes {

    private Indexes() {
        throw new AssertionError("No org.echo.data.index.Indexes instances for you!");
    }

    /**
     * 汇总
     *
     * @param title title of the result
     * @param <T> the type of the input elements
     * @return a int value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Integer> total(String title){
        return total(title, t -> true);
    }

    /**
     * 汇总
     *
     * @param title title of the result
     * @param condition a function testing the input element can be used
     * @param <T> the type of the input elements
     * @return a int value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Integer> total(String title, Predicate<T> condition){
        return new BiIndex<>(title, condition,
                () -> new int[1],
                (a, t) -> a[0] = a[0] + 1,
                (a1, a2) -> {
                    a1[0] = a1[0] + a2[0];
                    return a1;},
                a -> a[0]);
    }

    /**
     * 计算double类型和
     *
     * @param title title of the result
     * @param mapper a function extracting the property to be averaged
     * @param <T> the type of the input elements
     * @return a double value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Double> sumOfDouble(String title, ToDoubleFunction<T> mapper){
        return sumOfDouble(title, t -> true, mapper);
    }

    /**
     * 计算double类型和
     *
     * @param title title of the result
     * @param condition a function testing the input element can be used
     * @param mapper a function extracting the property to be averaged
     * @param <T> the type of the input elements
     * @return a double value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Double> sumOfDouble(String title, Predicate<T> condition, ToDoubleFunction<T> mapper){
        return new BiIndex<>(title,condition,
                () -> new double[1],
                (a, t) -> a[0] = a[0] + mapper.applyAsDouble(t),
                (a1,a2) -> {
                    a1[0] += a2[0];
                    return a1;
                },
                a->a[0]) ;
    }

    /**
     * 计算平均数
     *
     * @param title title of the result
     * @param mapper a function extracting the property to be averaged
     * @param <T> the type of the input elements
     * @return a double value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Double> average(String title, ToDoubleFunction<T> mapper){
        return average(title, t -> true, mapper);
    }

    /**
     * 计算平均数
     *
     * @param title title of the result
     * @param condition a function testing the input element can be used
     * @param mapper a function extracting the property to be averaged
     * @param <T> the type of the input elements
     * @return a double value when call {@link Index#getValue()}
     */
    public static <T> Index<T, Double> average(String title, Predicate<T> condition, ToDoubleFunction<T> mapper){
        return new BiIndex<>(title,condition,
                () -> new double[2],
                (a, t) -> { a[0] += mapper.applyAsDouble(t); a[1]++; },
                (a, b) -> { a[0] += b[0]; a[1] += b[1]; return a; },
                a -> (a[1] == 0) ? 0.0d : a[0] / a[1]);
    }
}
