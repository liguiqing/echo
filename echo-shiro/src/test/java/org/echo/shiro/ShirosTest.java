package org.echo.shiro;

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.echo.util.ClassUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/

@DisplayName("Shiros Test")
class ShirosTest {

    @BeforeEach
    public void before(){
        SecurityManager securityManager = mock(SecurityManager.class);
        ThreadContext.put(ThreadContext.SECURITY_MANAGER_KEY,securityManager);
        Subject subject = mock(Subject.class);
        when(subject.isAuthenticated()).thenReturn(true).thenReturn(false);
        ThreadContext.put(ThreadContext.SUBJECT_KEY,subject);
        assertThrows(Exception.class,() -> ClassUtils.newInstanceOf(Shiros.class));
    }

    @Test
    void getSubject() {
        assertNotNull(Shiros.getSubject());
    }

    @Test
    void isAuthenticated() {
        assertTrue(Shiros.isAuthenticated());
        assertFalse(Shiros.isAuthenticated());
    }
}