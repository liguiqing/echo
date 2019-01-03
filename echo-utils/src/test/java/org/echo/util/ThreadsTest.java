package org.echo.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : NumbersUtil test")
class ThreadsTest {
    @Test
    void test(){
        ExecutorService es1 = Threads.getExecutorService();
        ExecutorService es2 = Threads.getExecutorService();
        assertNotNull(es1);
        assertNotNull(es2);
        es1.shutdown();
        es2.shutdown();
        ExecutorService es3 = Threads.getExecutorService();
        assertNotNull(es3);
        es3.shutdown();
    }
}