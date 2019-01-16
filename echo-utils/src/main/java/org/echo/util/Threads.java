package org.echo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程工具
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Threads {
    private Threads(){
        throw new AssertionError("No org.echo.util.Threads instances for you!");
    }

    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService(){
        return service;
    }

}