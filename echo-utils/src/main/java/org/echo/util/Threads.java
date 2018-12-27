package org.echo.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 多线程工具
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Threads {

    //TODO
    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService(){
        return service;
    }

    public static Future submit(Runnable runnable){
        return getExecutorService().submit(runnable);
    }
}