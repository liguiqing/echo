package org.echo.ddd.support.infrastructure.id;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface WordsToString {

    String toString(String[] words, int length, PrefixExists<Boolean, String> callback);
}