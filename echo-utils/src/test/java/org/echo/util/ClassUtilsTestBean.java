package org.echo.util;

import java.lang.reflect.InvocationTargetException;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class ClassUtilsTestBean {

    public ClassUtilsTestBean() {
    }

    public ClassUtilsTestBean(String s) {
    }

    private String m1(){
        return "m1";
    }

    private void m2(String s,Long l){

    }

    private String[] m3(String s){
        return new String[]{s};
    }

    private void m44()throws IllegalAccessException{
        throw  new IllegalAccessException();
    }

    private void m45()throws InvocationTargetException {
        throw  new InvocationTargetException(new NullPointerException(""));
    }

    public String s1(){
        return "s1";
    }

    public String s1(String s){
        return s;
    }
}