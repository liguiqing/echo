/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 *
 */

package org.echo.web.servlet;


import org.echo.util.ClassUtils;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Echo : UserAgentUtils Test ")
@WebAppConfiguration
@EnableWebMvc
class ServletsTest  {

    @Test
    void test(){
        assertThrows(Exception.class,()-> ClassUtils.newInstanceOf(Servlets.class));
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

        when(request.getHeader(anyString())).thenReturn("header").thenReturn("").thenReturn(null);
        assertTrue(Servlets.requestHeaderContains(request, "header"));
        assertFalse(Servlets.requestHeaderContains(request, "header"));
        assertFalse(Servlets.requestHeaderContains(request, "header"));
    }
}