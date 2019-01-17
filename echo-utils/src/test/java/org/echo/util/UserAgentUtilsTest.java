package org.echo.util;

import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.DeviceType;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : UserAgentUtils Test ")
class UserAgentUtilsTest {
    HttpServletRequest request;
    @BeforeEach
    void before(){
        request = mock(HttpServletRequest.class);
        when(request.getHeader("User-Agent"))
                .thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50 micromessenger")
                .thenReturn("Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5 micromessenger")
                .thenReturn("Mozilla/5.0 (iPad; U; CPU OS 4_3_3 like Mac OS X; en-us) AppleWebKit/533.17.9 (KHTML, like Gecko) Version/5.0.2 Mobile/8J2 Safari/6533.18.5 ");
    }

    @Test
    void getUserAgent() {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(UserAgentUtils.class));
       // HttpServletRequest request = mock(HttpServletRequest.class);
        //when(request.getHeader("User-Agent")).thenReturn("Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10_6_8; en-us) AppleWebKit/534.50 (KHTML, like Gecko) Version/5.1 Safari/534.50");
        UserAgent agent = UserAgentUtils.getUserAgent(request);
        assertEquals(Browser.SAFARI5,agent.getBrowser());
        assertEquals(OperatingSystem.MAC_OS_X,agent.getOperatingSystem());
    }

    @Test
    void getDeviceType() {
        assertEquals(DeviceType.COMPUTER,UserAgentUtils.getDeviceType(request));
        assertEquals(DeviceType.MOBILE,UserAgentUtils.getDeviceType(request));
        assertEquals(DeviceType.TABLET,UserAgentUtils.getDeviceType(request));
    }

    @Test
    void isComputer() {
        assertTrue(UserAgentUtils.isComputer(request));
        assertFalse(UserAgentUtils.isComputer(request));
        assertFalse(UserAgentUtils.isComputer(request));
    }

    @Test
    void isMobile() {
        assertFalse(UserAgentUtils.isMobile(request));
        assertTrue(UserAgentUtils.isMobile(request));
        assertFalse(UserAgentUtils.isMobile(request));
    }

    @Test
    void isTablet() {
        assertFalse(UserAgentUtils.isTablet(request));
        assertFalse(UserAgentUtils.isTablet(request));
        assertTrue(UserAgentUtils.isTablet(request));
    }

    @Test
    void isMobileOrTablet() {
        assertFalse(UserAgentUtils.isMobileOrTablet(request));
        assertTrue(UserAgentUtils.isMobileOrTablet(request));
        assertTrue(UserAgentUtils.isMobileOrTablet(request));
    }

    @Test
    void isBrowser() {
        assertFalse(UserAgentUtils.isBrowser(null));
        assertTrue(UserAgentUtils.isBrowser(request));
        assertTrue(UserAgentUtils.isBrowser(request));
        assertTrue(UserAgentUtils.isBrowser(request));
    }

    @Test
    void isWeChat() {
        assertTrue(UserAgentUtils.isWeChat(request));
        assertTrue(UserAgentUtils.isWeChat(request));
        assertFalse(UserAgentUtils.isWeChat(request));
    }
}