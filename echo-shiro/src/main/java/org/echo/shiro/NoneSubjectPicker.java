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


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.subject.Subject;

/**
 * <p>
 * 无定义的SubjectPicker
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-15 08:14
 * @since V1.0.0
 **/
@Slf4j
@AllArgsConstructor
public class NoneSubjectPicker implements SubjectPicker {

    private Object o;

    private Subject subject;

    @Override
    public String getName() {
        return subjectToString(o);
    }

    @Override
    public String getAlias() {
        return subjectToString(o);
    }

    @Override
    public boolean isAuthenticated() {
        return subject.isAuthenticated();
    }

    private String subjectToString(Object o){
        log.warn("Can't find any Piker of {},return toString as default",o==null?"Null":o.getClass().getName());
        return subject.toString();
    }
}
