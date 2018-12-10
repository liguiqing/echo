package org.echo.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 多线程工具
 *
 * @author Liguiqing
 * @since V1.0
 */

public class Threads {

    //TODO
    private static final ExecutorService service = Executors.newCachedThreadPool();

    public static ExecutorService getExecutorService(){
        return service;
    }
}