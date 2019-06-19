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

package org.echo.web.servlet.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@DisplayName("Echo : AbstractHttpController Test")
class HttpControllerTest {

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