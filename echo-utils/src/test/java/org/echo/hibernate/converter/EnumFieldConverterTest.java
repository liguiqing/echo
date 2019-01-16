package org.echo.hibernate.converter;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.DynamicParameterizedType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.Types;
import java.time.LocalDate;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : EnumFieldConverter test")
class EnumFieldConverterTest {

    @Test
    void setParameterValues() {
        Properties properties = new Properties();
        properties.setProperty("org.hibernate.type.ParameterType", "");
        EnumFieldConverter converter = new EnumFieldConverter();
        assertEquals(Types.INTEGER,converter.sqlTypes()[0]);
        converter.setParameterValues(properties);
        assertNull(converter.returnedClass());
        DynamicParameterizedType.ParameterType parameterType = mock(DynamicParameterizedType.ParameterType.class);
        when(parameterType.getReturnedClass()).thenReturn(HibernateEnumIntegerBean.class);
        properties.put("org.hibernate.type.ParameterType",parameterType);
        converter.setParameterValues(properties);
        assertNotNull(converter.returnedClass());
        assertEquals(HibernateEnumIntegerBean.class, converter.returnedClass());
        assertEquals("",converter.deepCopy(""));
        assertFalse(converter.isMutable());
        assertEquals("a",converter.replace("a","b","c"));
        assertEquals("a",converter.assemble("a","B"));
        assertEquals("a",converter.disassemble("a"));
        assertTrue(converter.equals("a","a"));
        assertFalse(converter.equals("A","b"));
        assertEquals("a".hashCode(),converter.hashCode("a"));
        assertEquals(0,converter.hashCode(null));
    }

    @Test
    void nullSafeGet()throws Exception {
        ResultSet rs = mock(ResultSet.class);
        when(rs.getString(any(String.class))).thenReturn(null).thenReturn("1").thenReturn("2").thenReturn("3");
        SharedSessionContractImplementor session = mock(SharedSessionContractImplementor.class);

        EnumFieldConverter converter = new EnumFieldConverter();
        DynamicParameterizedType.ParameterType parameterType = mock(DynamicParameterizedType.ParameterType.class);
        when(parameterType.getReturnedClass()).thenReturn(HibernateEnumIntegerBean.class);
        Properties properties = new Properties();
        properties.put("org.hibernate.type.ParameterType",parameterType);
        converter.setParameterValues(properties);

        Object o = converter.nullSafeGet(rs,new String[]{"a"},session,"");
        assertNull(o);

        assertEquals(HibernateEnumIntegerBean.One,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertEquals(HibernateEnumIntegerBean.Two,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertThrows(HibernateException.class, () ->converter.nullSafeGet(rs,new String[]{"a"},session,""));

        parameterType = mock(DynamicParameterizedType.ParameterType.class);
        when(rs.getString(any(String.class))).thenReturn("1").thenReturn("2").thenReturn("3");
        when(parameterType.getReturnedClass()).thenReturn(HibernateEnumLongBean.class);
        properties.put("org.hibernate.type.ParameterType",parameterType);
        converter.setParameterValues(properties);

        assertEquals(HibernateEnumLongBean.One,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertEquals(HibernateEnumLongBean.Two,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertThrows(HibernateException.class, () ->converter.nullSafeGet(rs,new String[]{"a"},session,""));


        parameterType = mock(DynamicParameterizedType.ParameterType.class);
        when(rs.getString(any(String.class))).thenReturn(LocalDate.now().toString()).thenReturn(LocalDate.now().plusDays(1).toString()).thenReturn(LocalDate.now().plusDays(2).toString());
        when(parameterType.getReturnedClass()).thenReturn(HibernateEnumStringBean.class);
        properties.put("org.hibernate.type.ParameterType",parameterType);
        converter.setParameterValues(properties);

        assertEquals(HibernateEnumStringBean.Today,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertEquals(HibernateEnumStringBean.Tomorrow,converter.nullSafeGet(rs,new String[]{"a"},session,""));
        assertThrows(HibernateException.class, () ->converter.nullSafeGet(rs,new String[]{"a"},session,""));
    }

    @Test
    void nullSafeSet() {

    }

}