package org.echo.shiro.web.session.mgt;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("ServletUtilWrapper Test")
class ServletUtilWrapperTest {

    @Test
    void getRequest() {
        assertThrows(Exception.class,()->new PrivateConstructors().exec(ServletUtilWrapper.class));
        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
        RequestContextHolder.setRequestAttributes(attributes);
        assertNull(ServletUtilWrapper.getRequest());
    }

    @Test
    void getResponse() {
        ServletRequestAttributes attributes = mock(ServletRequestAttributes.class);
        RequestContextHolder.setRequestAttributes(attributes);
        assertNull(ServletUtilWrapper.getResponse());
    }
}