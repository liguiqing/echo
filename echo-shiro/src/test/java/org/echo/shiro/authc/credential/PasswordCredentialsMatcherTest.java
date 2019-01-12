package org.echo.shiro.authc.credential;

import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("PasswordCredentialsMatcher Test")
class PasswordCredentialsMatcherTest {

    @Test
    void doCredentialsMatch() {
        MD5PasswordEncoder encoder = new MD5PasswordEncoder();
        PasswordCredentialsMatcher matcher = new PasswordCredentialsMatcher(encoder);
        UsernamePasswordToken token = mock(UsernamePasswordToken.class);
        AuthenticationInfo info = mock(AuthenticationInfo.class);
        PrincipalCollection pc = mock(PrincipalCollection.class);
        when(pc.getPrimaryPrincipal()).thenReturn(new Principal()).thenReturn(new PasswordCredentialsMatcherTest());
        when(info.getPrincipals()).thenReturn(pc);
        when(info.getCredentials()).thenReturn(encoder.encode("salt","password"));
        when(token.getPassword()).thenReturn("password".toCharArray());
        assertTrue(matcher.doCredentialsMatch(token, info));
        assertFalse(matcher.doCredentialsMatch(token, info));
    }
}