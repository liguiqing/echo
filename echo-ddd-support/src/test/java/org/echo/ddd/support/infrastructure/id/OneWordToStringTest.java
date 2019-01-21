package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.domain.id.IdPrefixGeneratorNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : OneWordToString Test")
class OneWordToStringTest {

    @Test
    void toString1() {
        String[] className = {"Test"};
        PrefixExists exists = mock(PrefixExists.class);
        when(exists.callback(any(Object.class))).thenReturn(false).thenReturn(true)
                .thenReturn(false).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(true).thenReturn(true).thenReturn(false)
                .thenReturn(true).thenReturn(false);
        OneWordToString oneWordToString = new OneWordToString(null);

        assertEquals("TES",oneWordToString.toString(new String[]{"Test"},3,exists));
        assertEquals("TET",oneWordToString.toString(new String[]{"Test"},3,exists));
        assertEquals("TE1",oneWordToString.toString(new String[]{"Tes"},3,exists));
        assertEquals("TE2",oneWordToString.toString(new String[]{"Tes"},3,exists));
        assertEquals("TE3",oneWordToString.toString(new String[]{"Te"},3,exists));
        assertEquals("TT1",oneWordToString.toString(new String[]{"T"},3,exists));

        WordsToString nextWordsToString = mock(WordsToString.class);
        when(nextWordsToString.toString(any(String[].class), any(Integer.class), any(PrefixExists.class))).thenReturn("TET");
        oneWordToString = new OneWordToString(nextWordsToString);
        assertEquals("TET",oneWordToString.toString(new String[]{"Test","Test"},3,exists));

        assertThrows(IdPrefixGeneratorNotFoundException.class,()->new OneWordToString(null).toString(new String[]{"Test","Test"},3,exists));
        assertThrows(IdPrefixGeneratorNotFoundException.class,()->new OneWordToString(null).toString(new String[]{},3,exists));
    }
}