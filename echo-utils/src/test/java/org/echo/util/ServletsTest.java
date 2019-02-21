package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.echo.test.web.AbstractSpringControllerTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Copyright (c) 2016,$today.year, 深圳市易考试乐学测评有限公司
 **/
@DisplayName("Echo : UserAgentUtils Test ")
@WebAppConfiguration
@EnableWebMvc
class ServletsTest extends AbstractSpringControllerTest {

    @BeforeEach
    public void before(){
        super.before();
    }

    @Test
    void test(){
        assertThrows(Exception.class,()->new PrivateConstructors().exec(Servlets.class));
        assertTrue(true);

        HttpServletRequest request = mock(HttpServletRequest.class);
        ServletRequestAttributes attributes = new ServletRequestAttributes(request);
        RequestContextHolder.setRequestAttributes(attributes);
        assertNotNull(Servlets.getRequest());
        assertNull(Servlets.getResponse());

        HashMap<String,String[]> pss = new HashMap<>();
        pss.put("a",new String[]{"1"});
        pss.put("b",new String[]{"b"});
        pss.put("d",new String[]{});
        pss.put("e",null);
        pss.put("c",new String[]{LocalDate.now().toString()});
        when(request.getParameterMap()).thenReturn(pss).thenReturn(new HashMap<>());
        Map<String,String> map =  Servlets.getParameterMap(request);
        assertTrue(map.containsValue("1"));
        assertTrue(map.containsValue("b"));
        assertTrue(map.containsValue(LocalDate.now().toString()));
        assertEquals(0,Servlets.getParameterMap(request).size());
    }
}