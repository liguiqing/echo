package org.echo.share.web.servlet.handler;

import org.echo.share.web.servlet.http.ResponseText;
import org.echo.share.web.servlet.http.ResponseTextFactory;
import org.echo.share.web.servlet.http.Responser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : SpringMvcExceptionResolver Test")
class SpringMvcExceptionResolverTest {

    @Test
    void doResolveException() {
        ResponseTextFactory responseTextFactory = mock(ResponseTextFactory.class);
        ResponseText responseText = mock(ResponseText.class);
        when(responseTextFactory.lookup(any(String.class))).thenReturn(responseText);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        SpringMvcExceptionResolver resolver = spy(new SpringMvcExceptionResolver(responseTextFactory));
        Object o = new Object();
        when(request.getParameter(any(String.class))).thenReturn("zh_cn");
        Exception ex = new Exception("00.00.01");
        String viewName = "viewName";
        when(resolver.determineViewName(ex, request)).thenReturn(viewName).thenReturn("test");

        resolver.addStatusCode(viewName,200);
        ModelAndView mav = resolver.doResolveException(request, response, o,ex);
        Responser responser = (Responser)mav.getModel().get("status");
        assertFalse(responser.isSuccess());
        assertEquals("00.00.01",responser.getCode());
        mav = resolver.doResolveException(request, response, o,ex);
        ResponseText responseText1 = new ResponseText(){};
        assertEquals("12345",responseText1.getText("12345"));
        ResponseTextFactory factory = new ResponseTextFactory(){};
        ResponseText responseText2 = factory.lookup("zh_cn");
        assertNotNull(responseText2);
    }
}