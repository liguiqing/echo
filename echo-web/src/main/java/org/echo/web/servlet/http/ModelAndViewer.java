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

import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class ModelAndViewer {

    private HashMap<String, Object> model;

    private String viewName;

    private String code;

    private boolean success = true;

    private ResponseText responseText;

    public ModelAndViewer(String viewName, ResponseText responseText) {
        this.viewName = viewName;
        this.responseText = responseText;
        this.model = new HashMap<>();
    }

    public ModelAndViewer data(String name,Object data){
        this.model.put(name,data);
        return this;
    }

    public ModelAndViewer code(String code){
        this.code = code;
        return this;
    }

    public ModelAndViewer failure(){
        this.success = false;
        return this;
    }

    public ModelAndView create(){
        Responser response = new Responser(this.success,this.code,this.responseText.getText(this.code));
        this.data("status", response);
        return new ModelAndView(this.viewName, this.model);
    }
}