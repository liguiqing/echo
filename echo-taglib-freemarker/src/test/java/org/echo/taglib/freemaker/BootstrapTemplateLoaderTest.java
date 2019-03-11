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

package org.echo.taglib.freemaker;

import org.echo.share.config.SpringMvcConfiguration;
import org.echo.test.web.AbstractSpringControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;

import java.io.BufferedReader;
import java.io.Reader;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@DisplayName("Echo: BootstrapTemplateLoader Test")
@ContextHierarchy(@ContextConfiguration(
        classes = {
                SpringMvcConfiguration.class,
                BootstrapTemplateLoader.class,
                FreemarkerLoaderTestController.class
        }))
class BootstrapTemplateLoaderTest extends AbstractSpringControllerTest {

    @Test
    void html()throws Exception{
        this.mvc.perform(get("/html"))
                .andDo(print())
                .andExpect(view().name("/htmlTest"))
                .andExpect(content().string(startsWith("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("</html>")));

        this.mvc.perform(get("/html").param("requirejs","false"))
                .andDo(print())
                .andExpect(view().name("/htmlTest"))
                .andExpect(content().string(startsWith("<!DOCTYPE html>")))
                .andExpect(content().string(containsString("</html>")));
    }

    @Test
    void test() throws Exception {
        assertTrue(Boolean.TRUE);
        BootstrapTemplateLoader templateLoader = new BootstrapTemplateLoader();
        Object source = templateLoader.findTemplateSource("html.ftl");
        Reader reader = templateLoader.getReader(source, "utf-8");
        String s = readerToString(reader);
        assertNotNull(s);
    }

    private String readerToString(Reader reader) throws Exception {
        BufferedReader bufferedReader = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        String temp = null;
        while ((temp = bufferedReader.readLine()) != null) {
            sb.append(temp);
        }
        bufferedReader.close();
        return sb.toString();
    }
}