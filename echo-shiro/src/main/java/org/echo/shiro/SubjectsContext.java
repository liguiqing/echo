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

package org.echo.shiro;


import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.echo.util.ClassUtils;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * <p>
 * Subject 上下文查找
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-08 16:50
 * @since V1.0.0
 **/
@Slf4j
public class SubjectsContext {

    /**
     * 查找SubjectPicker
     *
     * @return Implements of {@link SubjectPicker}
     */
    public SubjectPicker lookup(){
        Object o = SecurityUtils.getSubject().getPrincipals().getPrimaryPrincipal();
        Class<? extends SubjectPicker> c = SubjectPicker.class;
        Method method = ClassUtils.findMethodOfReturns(o, c);
        if(Objects.isNull(method)){
            return new SubjectPicker() {};
        }
        Class cls = (Class)ClassUtils.invoke(o, method.getName());
        if(Objects.isNull(cls)){
            return new SubjectPicker() {};
        }

        return ClassUtils.newInstanceOf(cls);
    }
}
