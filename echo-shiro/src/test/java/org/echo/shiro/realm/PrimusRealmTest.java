package org.echo.shiro.realm;

import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.PrincipalCollection;
import org.echo.shiro.authc.credential.MD5PasswordEncoder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("PrimusRealm test")
class PrimusRealmTest {

    @Test
    void getName() {
        PrimusRealm realm = new PrimusRealm(new MD5PasswordEncoder());
        assertEquals("PrimusRealm",realm.getName());
    }

    @Test
    void supports() {
        AuthenticationToken token = mock(AuthenticationToken.class);
        assertTrue(new PrimusRealm(new MD5PasswordEncoder()).supports(token));
    }

    @Test
    void doGetAuthenticationInfo() {
        UsernamePasswordToken token = mock(UsernamePasswordToken.class);
        when(token.getPassword()).thenReturn("password".toCharArray());
        PrimusRealm realm = new PrimusRealm(new MD5PasswordEncoder());
        assertThrows(UnknownAccountException.class,()->realm.doGetAuthenticationInfo(token));
        when(token.getUsername()).thenReturn("Megatron");
        realm.doGetAuthenticationInfo(token);
        assertEquals("Megatron",realm.getMegatron().getName());
        assertEquals("威震天",realm.getMegatron().getRealName());
        assertTrue(realm.getMegatron().toString().contains("威震天"));
    }

    @Test
    void doGetAuthorizationInfo() {
        PrincipalCollection principals = mock(PrincipalCollection.class);
        assertNotNull(new PrimusRealm(new MD5PasswordEncoder()).doGetAuthorizationInfo(principals));
    }
}