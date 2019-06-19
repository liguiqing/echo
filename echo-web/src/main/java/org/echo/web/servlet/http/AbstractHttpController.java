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

import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.shiro.SubjectsContext;
import org.echo.web.servlet.Servlets;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @author Liguiqing
 * @since V1.0
 */

@Slf4j
public abstract class AbstractHttpController {

    @Autowired
    protected ResponseTextFactory responseTextFactory;

    @Autowired(required = false)
    protected SubjectsContext subjectsContext;

    protected void output(String content){
        this.output(content, Servlets.getResponse());
    }

    protected void output(String content, HttpServletResponse response){
        try {
            PrintWriter out = response.getWriter();
            out.print(content);
        } catch (IOException e) {
            log.error(ThrowableToString.toString(e));
        }
    }

    protected ModelAndViewer modelAndViewer(String viewName){
        String local = Servlets.getRequest().getParameter("local");
        return new ModelAndViewer(viewName,responseTextFactory.lookup(local));
    }
}