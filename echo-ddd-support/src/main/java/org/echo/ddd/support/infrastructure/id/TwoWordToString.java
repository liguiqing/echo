package org.echo.ddd.support.infrastructure.id;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class TwoWordToString implements WordsToString {

    private WordsToString nextWordsToString;

    @Override
    public String toString(String[] words, int length, PrefixExists<Boolean, String> callback) {
        return null;
    }
}