package org.echo.share.web.servlet.http;

import org.echo.share.config.SpringMvcConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@ContextHierarchy(@ContextConfiguration(classes = {
        SpringMvcConfiguration.class
}))
@WebAppConfiguration
@EnableWebMvc
@DisplayName("Echo : AbstractHttpController Test")
class AbstractHttpControllerTest {

    @BeforeEach
    public void beforeEach()throws Exception{
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        PrintWriter out = mock(PrintWriter.class);
        when(response.getWriter()).thenReturn(out).thenThrow(new IOException());
        ServletRequestAttributes attributes = spy(new ServletRequestAttributes(request,response));
        RequestContextHolder.setRequestAttributes(attributes);
    }

    @Test
    void output() {
        AbstractHttpController controller = new AbstractHttpController(){};
        controller.output("Test");
        controller.output("Test");
    }

    @Test
    void modelAndViewer() {
        AbstractHttpController controller = new AbstractHttpController(){};
        ResponseTextFactory responseTextFactory = mock(ResponseTextFactory.class);
        ResponseText text = mock(ResponseText.class);
        when(responseTextFactory.lookup(any())).thenReturn(text);
        controller.responseTextFactory = responseTextFactory;
        ModelAndViewer viewer = controller.modelAndViewer("/test");
        assertEquals("/test", viewer.create().getViewName());
        controller.modelAndViewer("/test");
    }
}