package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : ClassUtils Test")
class ClassUtilsTest {

    @Test
    void isParameterizedTypeOf() {
        assertTrue(ClassUtils.isParameterizedTypeOf(ClassUtilsTestInterFaceImpl.class,ClassUtilsTestBean.class));
        assertTrue(ClassUtils.isParameterizedTypeOf(new ClassUtilsTestInterFaceImpl().getClass(),ClassUtilsTestBean.class));
        assertFalse(ClassUtils.isParameterizedTypeOf(ClassUtilsTestInterFaceImpl.class,String.class));
        assertFalse(ClassUtils.isParameterizedTypeOf(ClassUtilsTestInterFaceImpl2.class,ClassUtilsTestBean.class));

        AbstractClassUtilsTestInterFace2<String> testO = new AbstractClassUtilsTestInterFace2<String>(){};
        assertTrue(ClassUtils.isParameterizedTypeOf(testO.getClass(),String.class));

        assertThrows(Exception.class,()->new PrivateConstructors().exec(ClassUtils.class));
    }

    @Test
    void invoke(){
        ClassUtilsTestBean bean1 = new ClassUtilsTestBean();
        assertNotNull(ClassUtils.invoke(bean1,"m1"));
        ClassUtils.invoke(bean1,"m2","a",1L);
        Object oo = ClassUtils.invoke(bean1,"m3","a");
        assertTrue(oo instanceof String[]);
        assertNull(ClassUtils.invoke(bean1,"m2","a",1F));
        assertNull(ClassUtils.invoke(bean1,"m44"));
        assertNull(ClassUtils.invoke(bean1,"m45"));
    }
}