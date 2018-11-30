package org.echo.util;

import java.util.Random;

/**
 * 数字工具
 *
 * @author Liguiqing
 * @since V1.0
 */
public class NumbersUtil {

    /**
     * 取两整数间随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int randomBetween(int min, int max) {
        return new Random().ints(min, (max + 1)).findFirst().getAsInt();
    }
}