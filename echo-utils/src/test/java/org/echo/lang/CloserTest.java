package org.echo.lang;

import org.echo.exception.BusinessException;
import org.echo.util.ClassUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Closeable;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : CloserTest Test")
class CloserTest {

    @Test
    void close() throws Exception{
        Closeable closeable = mock(Closeable.class);
        Closer.close(closeable);
        doThrow(IOException.class).when(closeable).close();
        Closer.close(closeable);
        assertThrows(BusinessException.class,()-> ClassUtils.newInstanceOf(Closer.class));
    }
}