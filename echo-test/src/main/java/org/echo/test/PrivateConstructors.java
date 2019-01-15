package org.echo.test;

import java.lang.reflect.Constructor;

/**
 * 私有构造调用
 *
 * @author Liguiqing
 * @since V1.0
 */

public class PrivateConstructors {


    public void exec(Class clazz)throws Exception{
        Constructor c0=  clazz.getDeclaredConstructor();
        c0.setAccessible(true);
        c0.newInstance();
    }
}