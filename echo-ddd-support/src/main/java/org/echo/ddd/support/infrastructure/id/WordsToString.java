package org.echo.ddd.support.infrastructure.id;

/**
 *  将String数组按某种规则转换为　String
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface WordsToString {

    /**
     *
     * @param words 待转换的单词数组
     * @param length　转换长度
     * @param callback　是否存在判断函数
     * @return String
     */
    String toString(String[] words, int length, PrefixExists<Boolean, String> callback);
}