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


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.web.servlet.http.ModelAndViewer;
import org.echo.web.servlet.http.ResponseText;
import org.echo.web.servlet.http.ResponseTextFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
@AllArgsConstructor
public class SpringMvcExceptionResolver extends SimpleMappingExceptionResolver {

    private ResponseTextFactory responseTextFactory;

    @Override
    protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                              Exception ex) {
        log.error("系统异常 ：{}", ThrowableToString.toString(ex));
        String viewName = determineViewName(ex,request);

        Integer statusCode = determineStatusCode(request, viewName);
        if (statusCode != null) {
            applyStatusCodeIfPossible(request, response, statusCode);
        }
        return createModelAndView(viewName,request, ex);
    }

    @Override
    protected String determineViewName(Exception ex,HttpServletRequest request){
        log.debug("Get View Name");
        return super.determineViewName(ex, request);
    }

    private ModelAndView createModelAndView(String viewName, HttpServletRequest request, Exception ex) {
        String code = ex.getMessage();
        ResponseText responseText = responseTextFactory.lookup(request.getParameter("local"));
        return new ModelAndViewer(viewName,responseText).code(code).failure().create();
    }
}