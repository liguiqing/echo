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


import com.google.common.collect.Maps;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * <p>
 *
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-11 13:27
 * @since V1.0.0
 **/
@Controller
public class FreemarkerLoaderTestController {

    @RequestMapping(value="/html")
    public ModelAndView html(@RequestParam(required = false,defaultValue = "true") boolean requirejs){
        Map<String,Object> model = Maps.newHashMap();
        model.put("requireJs",requirejs);
        return new ModelAndView("/htmlTest",model);
    }
}
