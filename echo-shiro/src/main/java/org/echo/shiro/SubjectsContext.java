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


import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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

    private List<SubjectPicker> subjectPickers = Lists.newArrayList();

    public SubjectsContext(Optional<Collection<SubjectPicker>> pickers) {
        pickers.ifPresent(subjectPickers::addAll);
    }

    /**
     * 查找SubjectPicker
     *
     * @return Implements of {@link SubjectPicker}
     */
    public SubjectPicker lookup(){
        Subject subject = SecurityUtils.getSubject();
        if(!subject.isAuthenticated()){
            return new SubjectPicker() {};
        }

        Object o = getSubjectAuthenticated(subject);

        for(SubjectPicker picker:subjectPickers){
            if (picker.supports(o)){
                return picker;
            }
        }

        return  new NoneSubjectPicker(o, subject);
    }

    private Object getSubjectAuthenticated(Subject subject){
        Object o = subject.getPrincipals().getPrimaryPrincipal();
        if(Objects.isNull(o)) {
            o = subject.getPrincipal();
        }
        return o;
    }
}
