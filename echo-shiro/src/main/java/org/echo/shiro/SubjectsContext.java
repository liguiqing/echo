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
        pickers.ifPresent(sps->subjectPickers.addAll(sps));
    }

    /**
     * 查找SubjectPicker
     *
     * @return Implements of {@link SubjectPicker}
     */
    public SubjectPicker lookup(){
        Subject subject = SecurityUtils.getSubject();
        Object po = subject.getPrincipals().getPrimaryPrincipal();
        Object o = subject.getPrincipal();
        for(SubjectPicker picker:subjectPickers){
            if (picker.supports(po) || picker.supports(o)){
                return picker;
            }
        }

        if(!Objects.isNull(po)){
            return fromSubject(po, subject);
        }

        if(!Objects.isNull(o)){
            return fromSubject(o, subject);
        }

        return new SubjectPicker() {};
    }

    private SubjectPicker fromSubject(final Object o,final Subject subject){
        return new SubjectPicker() {
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
                log.warn("Can't find any Piker of {},return toString as default",o.getClass().getName());
                return subject.toString();
            }
        };
    }
}
