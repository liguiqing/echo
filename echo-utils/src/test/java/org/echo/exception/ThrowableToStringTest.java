package org.echo.exception;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : ThrowableToString Test")
class ThrowableToStringTest {

    @Test
    void toHtml() {
        Exception e1 = new Exception("aaa");
        String s = ThrowableToString.toHtml(e1);
        assertTrue(s.startsWith("<div"));
    }

    @Test
    void cleanExceptionString() {
        assertEquals("aaa",ThrowableToString.cleanExceptionString(new Exception("aaa")));
        assertEquals("you are fooled",ThrowableToString.cleanExceptionString(new Exception("Exception:you are fooled")));
        assertNull(ThrowableToString.cleanExceptionString(new Exception()));
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

        assertThrows(Exception.class,()->new PrivateConstructors().exec(ThrowableToString.class));
    }
}