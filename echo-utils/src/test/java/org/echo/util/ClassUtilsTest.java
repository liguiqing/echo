package org.echo.util;

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
        assertFalse(ClassUtils.isParameterizedTypeOf(ClassUtilsTestInterFaceImpl.class,String.class));
        assertFalse(ClassUtils.isParameterizedTypeOf(ClassUtilsTestInterFaceImpl2.class,ClassUtilsTestBean.class));
    }
}