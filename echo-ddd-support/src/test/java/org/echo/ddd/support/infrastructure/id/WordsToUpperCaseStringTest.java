package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.domain.id.IdPrefixGeneratorNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : WordsToUpperCaseString Test")
class WordsToUpperCaseStringTest {

    @Test
    void test(){
        assertTrue(Boolean.TRUE);

        PrefixExists exists = mock(PrefixExists.class);

        WordsToUpperCaseString wordToString = new WordsToUpperCaseString();

        when(exists.callback(any(Object.class))).thenReturn(false);
        assertEquals("TES",wordToString.toString(new String[]{"Test"},3,exists));
        when(exists.callback(any(Object.class))).thenReturn(true).thenReturn(false);
        assertEquals("TET",wordToString.toString(new String[]{"Test"},3,exists));
        when(exists.callback(any(Object.class))).thenReturn(true).thenReturn(false);
        assertEquals("TE1",wordToString.toString(new String[]{"Tes"},3,exists));
        when(exists.callback(any(Object.class))).thenReturn(true).thenReturn(true).thenReturn(false);
        assertEquals("TE2",wordToString.toString(new String[]{"Tes"},3,exists));
        when(exists.callback(any(Object.class))).thenReturn(true).thenReturn(true).thenReturn(false);
        assertEquals("TE3",wordToString.toString(new String[]{"Te"},3,exists));
        when(exists.callback(any(Object.class))).thenReturn(true).thenReturn(false);
        assertEquals("T12",wordToString.toString(new String[]{"T"},3,exists));
        when(exists.callback(any(Object.class)))
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(false);
        assertEquals("T29",wordToString.toString(new String[]{"T"},3,exists));
        when(exists.callback(any(Object.class)))
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(true).thenReturn(true)
                .thenReturn(true).thenReturn(false);
        assertEquals("T39",wordToString.toString(new String[]{"T"},3,exists));

        WordsToString nextWordsToString = mock(WordsToString.class);
        when(nextWordsToString.toString(any(String[].class), any(Integer.class), any(PrefixExists.class))).thenReturn("TET");

        assertThrows(IdPrefixGeneratorNotFoundException.class,()->new WordsToUpperCaseString().toString(null,3,exists));
        assertThrows(IdPrefixGeneratorNotFoundException.class,()->new WordsToUpperCaseString().toString(new String[]{},3,exists));
        assertThrows(IdPrefixGeneratorNotFoundException.class,()->new WordsToUpperCaseString().toString(new String[]{"Test","Test"},1,exists));

        when(exists.callback(any(Object.class))).thenReturn(false)
                .thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        WordsToUpperCaseString twoWordToString = new WordsToUpperCaseString();

        assertEquals("QTW",twoWordToString.toString(new String[]{"Question","Two"},3,exists));
        assertEquals("QTO",twoWordToString.toString(new String[]{"Question","Two"},3,exists));
        assertEquals("QT1",twoWordToString.toString(new String[]{"Question","Two"},3,exists));
        assertEquals("QT2",twoWordToString.toString(new String[]{"Question","Two"},3,exists));

        when(exists.callback(any(Object.class))).thenReturn(false)
                .thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        WordsToUpperCaseString threeWordToString = new WordsToUpperCaseString();

        assertEquals("QTA",threeWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTN",threeWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTS",threeWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTW",threeWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));

        twoWordToString = new WordsToUpperCaseString();
        when(exists.callback(any(Object.class))).thenReturn(false)
                .thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
                assertEquals("QTA",twoWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTN",twoWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTS",twoWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));
        assertEquals("QTW",twoWordToString.toString(new String[]{"Question","Two","Answer"},3,exists));

        WordsToUpperCaseString fourWordToString = new WordsToUpperCaseString();
        when(exists.callback(any(Object.class))).thenReturn(false)
                .thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        assertEquals("QTA",fourWordToString.toString(new String[]{"Question","Two","Answer","Fuck"},3,exists));
        assertEquals("QTF",fourWordToString.toString(new String[]{"Question","Two","Answer","Fuck"},3,exists));
        assertEquals("QTU",fourWordToString.toString(new String[]{"Question","Two","Answer","Fuck"},3,exists));
        assertEquals("QTC",fourWordToString.toString(new String[]{"Question","Two","Answer","Fuck"},3,exists));

        WordsToUpperCaseString moreWordToString = new WordsToUpperCaseString();
        when(exists.callback(any(Object.class))).thenReturn(false)
                .thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false);
        assertEquals("QTA",moreWordToString.toString(new String[]{"Question","Two","Answer","Fuck","You"},3,exists));
        assertEquals("QTF",moreWordToString.toString(new String[]{"Question","Two","Answer","Fuck","You"},3,exists));
        assertEquals("QTY",moreWordToString.toString(new String[]{"Question","Two","Answer","Fuck","You"},3,exists));
        assertEquals("QTO",moreWordToString.toString(new String[]{"Question","Two","Answer","Fuck","You"},3,exists));
    }
}