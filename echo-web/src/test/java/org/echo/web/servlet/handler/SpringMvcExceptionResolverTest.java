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

package org.echo.web.servlet.handler;

import org.echo.web.servlet.http.ResponseText;
import org.echo.web.servlet.http.ResponseTextFactory;
import org.echo.web.servlet.http.Responser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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