package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : NumbersUtil exec")
class NumbersUtilTest {


    @RepeatedTest(10)
    void randomBetween() {
        assertTrue(NumbersUtil.randomBetween(1,1)==1);
        assertTrue(NumbersUtil.randomBetween(1,2)>=1);
        assertTrue(NumbersUtil.randomBetween(1,2)<=2);
        assertTrue(NumbersUtil.randomBetween(0,1)>=0);
        assertTrue(NumbersUtil.randomBetween(0,1)<=1);
        assertTrue(NumbersUtil.randomBetween(-1,0)>=-1);
        assertTrue(NumbersUtil.randomBetween(-1,0)<=0);
        assertTrue(NumbersUtil.randomBetween(-2,-1)>=-2);
        assertTrue(NumbersUtil.randomBetween(-2,-1)<=-1);
        assertTrue(NumbersUtil.randomBetween(1,20000)>=1);
        assertTrue(NumbersUtil.randomBetween(1,20000)<=20000);
        assertThrows(IllegalArgumentException.class,() -> NumbersUtil.randomBetween(2,1),"bound must be greater than origin");
        assertThrows(IllegalArgumentException.class,() -> NumbersUtil.randomBetween(-2,-11),"bound must be greater than origin");
    }

    @Test
    void stringToInt() {
        assertTrue(NumbersUtil.stringToInt("1")==1);
        assertTrue(NumbersUtil.stringToInt("-1")==-1);
        assertTrue(NumbersUtil.stringToInt("0")==0);
        assertTrue(NumbersUtil.stringToInt("0.0")==0);
        assertTrue(NumbersUtil.stringToInt("0.5")==0);
        assertTrue(NumbersUtil.stringToInt("aaa")==0);

        assertThrows(Exception.class,()->new PrivateConstructors().exec(NumbersUtil.class));
    }

    @Test
    void stringToLong() {
        assertTrue(NumbersUtil.stringToLong("1")==1);
        assertTrue(NumbersUtil.stringToLong("-1")==-1);
        assertTrue(NumbersUtil.stringToLong("0")==0);
        assertTrue(NumbersUtil.stringToLong("0.0")==0);
        assertTrue(NumbersUtil.stringToLong("0.5")==0);
        assertTrue(NumbersUtil.stringToLong("aaa")==0);
    }
}