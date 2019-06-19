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

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */

public class Servlets {

    private Servlets(){
        throw new AssertionError("No org.echo.util.Servlets instances for you!");
    }

    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    public static Map<String,String> getParameterMap(HttpServletRequest request){
        Map<String,String[]> ps = request.getParameterMap();
        HashMap<String,String> pss = new HashMap<>();
        Iterator<String> keys = ps.keySet().iterator();
        while(keys.hasNext()){
            String key = keys.next();
            String[] values = ps.get(key);
            if(values == null || values.length == 0){
                pss.put(key,"");
            }else{
                pss.put(key,values[0]);
            }
        }
        return pss;
    }
}