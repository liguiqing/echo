package org.echo.ddd.infrastructure.id;

/**
 *
 * @author Liguiqing
 * @since V1.0
 */
@FunctionalInterface
public interface WordsToString {

    String toString(String[] words, int length, PrefixExistCallback<Boolean,String> callback);
}