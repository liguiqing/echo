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

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Objects;
import java.util.Properties;

/**
 * 枚举属性值(Field)转换器
 *
 * @author Liguiqing
 * @since V1.0
 */

public class EnumFieldConverter implements UserType, DynamicParameterizedType {

    private Class<HibernateEnum> enumClass;

    private static final int[] SQL_TYPES = new int[]{Types.INTEGER};

    @Override
    public void setParameterValues(Properties parameters) {
        Object o = parameters.get(PARAMETER_TYPE);
        if (o instanceof ParameterType) {
            ParameterType reader = (ParameterType) o;
            enumClass = reader.getReturnedClass().asSubclass(HibernateEnum.class);
        }
    }

    //枚举存储int值
    @Override
    public int[] sqlTypes() {
        return SQL_TYPES;
    }

    @Override
    public Class returnedClass() {
        return enumClass;
    }

    //是否相等，不相等会触发JPA update操作
    @Override
    public boolean equals(Object x, Object y) {
        return Objects.equals(x, y);
    }

    @Override
    public int hashCode(Object x) {
        return x == null ? 0 : x.hashCode();
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] names, SharedSessionContractImplementor session, Object owner) throws SQLException {
        String value = rs.getString(names[0]);
        if (value == null) {
            return null;
        }

        for (HibernateEnum object : enumClass.getEnumConstants()) {
            if (typeOf(object, Integer.class)) {
                if (Objects.equals(Integer.parseInt(value), object.getValue())) {
                    return object;
                }
            } else if (typeOf(object, Long.class)) {
                if (Objects.equals(Long.parseLong(value), object.getValue())) {
                    return object;
                }
            } else {
                if (Objects.equals(value, object.getValue())) {
                    return object;
                }
            }

        }
        throw new HibernateException(String.format("Unknown name value [%s] for enum class [%s]", value, enumClass.getName()));
    }

    @Override
    public void nullSafeSet(PreparedStatement st, Object value, int index, SharedSessionContractImplementor session) throws  SQLException {
        if (value == null) {
            st.setNull(index, SQL_TYPES[0]);
            return;
        } else if (value instanceof Integer) {
            st.setInt(index, (Integer) value);
            return;
        } else if (value instanceof HibernateEnum){
            if (typeOf(value, Integer.class)) {
                st.setInt(index, (Integer) ((HibernateEnum) value).getValue());
            } else if (typeOf(value, Long.class)) {
                st.setLong(index, (Long) ((HibernateEnum) value).getValue());
            }else if (typeOf(value, String.class)) {
                st.setString(index, (String) ((HibernateEnum) value).getValue());
            }else{
                st.setObject(index, ((HibernateEnum) value).getValue());
            }
            return;
        }
        throw new HibernateException(String.format("Unknown type value [%s] for class [%s]", value, value.getClass().getName()));
    }

    private boolean typeOf(Object object, Class clazz) {
        Type[] types = object.getClass().getGenericInterfaces();
        if (types.length > 0 && types[0] instanceof ParameterizedType ) {
            ParameterizedType p = (ParameterizedType) types[0];
            Class c = (Class) p.getActualTypeArguments()[0];
            return c.equals(clazz);
        }
        return false;
    }

    @Override
    public Object deepCopy(Object value){
        return value;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Override
    public Serializable disassemble(Object value){
        return (Serializable) value;
    }

    @Override
    public Object assemble(Serializable cached, Object owner) {
        return cached;
    }

    @Override
    public Object replace(Object original, Object target, Object owner){
        return original;
    }
}