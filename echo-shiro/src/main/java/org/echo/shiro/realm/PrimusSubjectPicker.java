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

package org.echo.shiro.realm;

import org.apache.shiro.SecurityUtils;
import org.echo.shiro.Shiros;
import org.echo.shiro.SubjectPicker;

/**
 * <p>
 * 测试用户名信息提取
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-08 16:09
 * @since V1.0.0
 **/
public class PrimusSubjectPicker implements SubjectPicker {
    @Override
    public String getName() {
        return getDecepticons().getName();
    }

    @Override
    public String getAlias() {
        return getDecepticons().getRealName();
    }

    @Override
    public boolean isAuthenticated() {
        return Shiros.isAuthenticated();
    }

    @Override
    public boolean supports(Object subject) {
        return subject instanceof Decepticons;
    }

    private Decepticons getDecepticons(){
        var subject = SecurityUtils.getSubject();
        return (Decepticons)subject.getPrincipal();
    }
}
