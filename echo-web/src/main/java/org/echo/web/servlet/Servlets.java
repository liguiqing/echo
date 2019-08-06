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

import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import org.echo.exception.ThrowableToString;
import org.echo.web.servlet.http.ModelAndViewer;
import org.echo.web.servlet.http.ResponseTextFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Liguiqing
 * @since V1.0
 */
@Slf4j
public class Servlets {

    private Servlets(){
        throw new AssertionError("No org.echo.util.Servlets instances for you!");
    }

    /**
     * 直接获取当前线程{@link HttpServletRequest}
     * @return {@link HttpServletRequest}
     */
    public static HttpServletRequest getRequest() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
    }

    /**
     * 直接获取当前线程{@link HttpServletResponse}
     * @return {@link HttpServletResponse}
     */
    public static HttpServletResponse getResponse() {
        return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
    }

    /**
     * 读取{@link HttpServletRequest}queryString的所有参数并转换{@link Map}中。
     * 如果参数有多个相同的值，只取首个。
     * @param request
     * @return a {@link Map}
     */
    public static Map<String,String> getParameterMap(HttpServletRequest request){
        Map<String,String[]> ps = request.getParameterMap();
        HashMap<String,String> pss = new HashMap<>();
        Iterator<String> keys = ps.keySet().iterator();
        keys.forEachRemaining(key -> {
            String[] values = ps.get(key);
            pss.put(key,(values == null || values.length == 0)?"":values[0]);

        });
        return pss;
    }

    /**
     * 强制响应{@link ModelAndView#getModel()}格式的json数据,同时关闭 {@link HttpServletResponse}
     * @param response {@link HttpServletResponse}
     * @param responseTextFactory {@link ResponseTextFactory}
     * @param respondStatus a status code of {@link HttpServletResponse} public field
     */
    public static void responseJsonAndClose(HttpServletResponse response, ResponseTextFactory responseTextFactory,
                                    int respondStatus){
        String local = getRequest().getParameter("local");
        ModelAndViewer modelAndViewer = new ModelAndViewer("",responseTextFactory.lookup(local));
        modelAndViewer.failure().code("http:" + respondStatus);
        response.setStatus(respondStatus);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        outputAndClose(JSONObject.toJSONString(modelAndViewer.create().getModel()),response);
    }

    /**
     * 响应{@link String},同时关闭 {@link HttpServletResponse}
     * @param content output content
     * @param response {@link HttpServletResponse}
     */
    public static void outputAndClose(String content, HttpServletResponse response){
        try(PrintWriter out = response.getWriter()) {
            out.print(content);
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        }
    }

    /**
     * 响应{@link String},不关闭 {@link HttpServletResponse}，客户端需要自行处理{@link HttpServletResponse}关闭
     * @param content output content
     * @param response {@link HttpServletResponse}
     */
    public static void outputNotClose(String content, HttpServletResponse response){
        try(PrintWriter out = response.getWriter()) {
            out.print(content);
        } catch (Exception e) {
            log.error(ThrowableToString.toString(e));
        }
    }

    /**
     * 检查{@link ServletRequest} header 中是否包含有名为header的参数
     * @param request a {@link ServletRequest}
     * @param header request header name
     * @return true if contains else false
     */
    public static boolean requestHeaderContains(ServletRequest request,String header){
        HttpServletRequest req = (HttpServletRequest) request;
        return !Strings.isNullOrEmpty(req.getHeader(header));
    }

    /**
     * 检查ServletRequest参数是否空/Null
     * @param request a {@link ServletRequest}
     * @param containsHeader 是否检查header
     * @param paramName 待查的参数名
     * @return true when any paramName is empty or null else false
     */
    public static boolean isParamNullOrEmpty(ServletRequest request,boolean containsHeader,String...paramName){
        HttpServletRequest req = (HttpServletRequest) request;
        boolean nullOrEmpty = Boolean.TRUE;
        for(String name:paramName){
            nullOrEmpty = Strings.isNullOrEmpty(req.getParameter(name));
            if(nullOrEmpty && containsHeader) nullOrEmpty = !requestHeaderContains(request, name);
            if(nullOrEmpty) break;
        }
        return nullOrEmpty;
    }
}