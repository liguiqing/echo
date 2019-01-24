package org.echo.ddd.support.infrastructure.id;

import org.echo.ddd.support.domain.model.id.IdPrefixBean;
import org.echo.ddd.support.domain.model.id.IdPrefixBeanRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@DisplayName("Echo : IdPrefixGenerator Test")
class IdPrefixGeneratorTest {

    @Test
    void of() throws Exception{
        WordsToString wordsToString = mock(WordsToString.class);
        IdPrefixBeanRepository repository = mock(IdPrefixBeanRepository.class);
        IdPrefixGenerator generator = new IdPrefixGenerator(3,wordsToString,repository);
        IdPrefixBean idPrefixBean = new IdPrefixBean(IdPrefixTestStringIdentity.class.getName(),"IPT", 1L);
        when(repository.loadOf(any(Integer.class))).thenReturn(idPrefixBean).thenReturn(null);
        when(wordsToString.toString(any(String[].class), any(Integer.class), any(PrefixExists.class))).thenReturn("IPT");

        generator.of(IdPrefixTestStringIdentity.class);
        assertEquals("IPT", generator.of(IdPrefixTestStringIdentity.class));
        assertEquals("IPT", generator.of(IdPrefixTestStringIdentity.class));

        when(repository.findByIdPrefix(any(String.class))).thenReturn(idPrefixBean).thenReturn(null);
        Method method = generator.getClass().getDeclaredMethod("prefixExists", String.class);
        method.setAccessible(true);
        assertTrue((Boolean) method.invoke(generator, "IPT"));
        assertFalse((Boolean) method.invoke(generator, "IPT"));
    }
}