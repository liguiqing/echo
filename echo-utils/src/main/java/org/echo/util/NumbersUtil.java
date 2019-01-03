package org.echo.util;

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;

import java.util.Random;

/**
 * 数字工具
 *
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class NumbersUtil {

    private NumbersUtil() {
        throw new IllegalArgumentException();
    }

    /**
     * 取两整数间随机数 开区间
     *
     * @param min　
     * @param max　
     * @return
     */
    public static int randomBetween(int min, int max) {
        return new Random().ints(min, (max + 1)).findFirst().getAsInt();
    }

    /**
     * String to Integer
     *
     * @param s a string will be convert
     * @return int  if convert failure return 0
     *
     */
    public static int stringToInt(String s){
        return stringToInt(s,0);
    }

    /**
     * String to Integer with default
     *
     * @param s a string will be convert
     * @param defaultValue returns if convert failure
     * @return int
     */
    public static int stringToInt(String s,int defaultValue){
        try {
            return Integer.parseInt(s);
        }catch (Exception ignored){
            log.error(ThrowableToString.toString(ignored));
        }
        return defaultValue;
    }

    /**
     * String to Long
     *
     * @param s a string will be convert
     * @return long  if convert failure return 0
     *
     */
    public static long stringToLong(String s){
        return stringToLong(s,0L);
    }

    /**
     * String to Long with default
     *
     * @param s a string will be convert
     * @param defaultValue returns if convert failure
     * @return long
     */
    public static long stringToLong(String s,long defaultValue){
        try {
            return Long.parseLong(s);
        }catch (Exception ignored){
            log.error(ThrowableToString.toString(ignored));
        }
        return defaultValue;
    }
}