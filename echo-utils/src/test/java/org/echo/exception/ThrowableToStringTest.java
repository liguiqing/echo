package org.echo.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : ThrowableToString test")
class ThrowableToStringTest {

    @Test
    void toHtml() {
        Exception e1 = new Exception("aaa");
        String s = ThrowableToString.toHtml(e1);
        assertTrue(s.startsWith("<div"));
    }

    @Test
    void cleanExceptionString() {
        Exception e1 = new Exception("aaa");
        String s = ThrowableToString.cleanExceptionString(e1);
        assertTrue(s.equals("aaa"));
        s = ThrowableToString.cleanExceptionString(new Exception());
        assertNull(s);
    }

    @Test
    void toString1() {
        Exception e1 = new Exception("aaa");
        String s = ThrowableToString.toString(e1);
        assertTrue(s.contains("aaa"));
        assertTrue(s.contains("Exception"));
        s = ThrowableToString.toString(new Throwable("aaa"));
        assertTrue(s.contains("aaa"));
        assertTrue(s.contains("Throwable"));
        assertTrue(ThrowableToString.toString(null).contains("Exception is null"));
        Exception e2 = mock(Exception.class);
        when(e2.getCause()).thenReturn(null);
        when(e2.getMessage()).thenReturn("Is a Exception");
        assertTrue(ThrowableToString.toString(e2).contains(""));
        Throwable t = null;
        assertTrue(ThrowableToString.toString(t).contains("Throwable is null"));
    }
}