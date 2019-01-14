package org.echo.shiro.session.mgt.eis;

import org.apache.shiro.session.Session;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("SessionIdGeneratorIterator Test")
class SessionIdGeneratorIteratorTest {

    @Test
    void generateId() {
        SessionIdGeneratorIterator sessionIdGenerator = new SessionIdGeneratorIterator(Optional.ofNullable(null));
        Session session = mock(Session.class);
        assertNotNull(sessionIdGenerator.generateId(session));

        sessionIdGenerator = new SessionIdGeneratorIterator(Optional.empty());
        assertNotNull(sessionIdGenerator.generateId(session));
        SessionIdGeneratorIterator m1 = mock(SessionIdGeneratorIterator.class);
        when(m1.generateId(any(Session.class))).thenReturn("1");
        sessionIdGenerator = new SessionIdGeneratorIterator(Optional.of(Arrays.asList(m1)));
        assertEquals("1",sessionIdGenerator.generateId(session));
    }
}