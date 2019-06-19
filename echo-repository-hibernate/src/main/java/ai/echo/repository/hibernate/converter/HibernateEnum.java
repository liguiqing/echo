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

package ai.echo.repository.hibernate.converter;

import java.io.Serializable;
import java.util.Objects;

/**
 * Hibernate 枚举转换
 *
 * @author Liguiqing
 * @since V1.0
 */

public interface HibernateEnum<V> extends Serializable {

    /**
     * 用于显示的枚举名时查询的键
     *
     * @return String
     */
    String getKey();

    /**
     * 存储到数据库的枚举值
     *
     * @return V
     */
    V getValue();

    /**
     * 按枚举的value获取枚举实例
     *
     * @param enumType class of enum
     * @param value enum's value
     * @param <T> enum type
     * @return enum instance ,throws IllegalArgumentException if value not find
     */
    static <T extends HibernateEnum,V> T fromValue(Class<T> enumType, V value) {
        for (T t : enumType.getEnumConstants()) {
            if (Objects.equals(value, t.getValue())) {
                return t;
            }
        }
        throw new IllegalArgumentException("No enum value " + value + " of " + enumType.getCanonicalName());
    }
}