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

import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.echo.shiro.Shiros;
import org.echo.util.ClassUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("PrimusSubjectPicker Test")
class PrimusSubjectPickerTest {

    @BeforeEach
    public void before(){
        SecurityManager securityManager = mock(SecurityManager.class);
        ThreadContext.put(ThreadContext.SECURITY_MANAGER_KEY,securityManager);
        Subject subject = mock(Subject.class);
        when(subject.isAuthenticated()).thenReturn(true).thenReturn(false);
        Decepticons decepticons = new Decepticons().megatron();
        when(subject.getPrincipal()).thenReturn(decepticons);
        ThreadContext.put(ThreadContext.SUBJECT_KEY,subject);
        assertThrows(Exception.class,()-> ClassUtils.newInstanceOf(Shiros.class));
    }


    @Test
    void test(){
        assertTrue(true);
        PrimusSubjectPicker subjectPicker = new PrimusSubjectPicker();
        assertTrue(subjectPicker.isAuthenticated());
        assertFalse(subjectPicker.isAuthenticated());
        Decepticons decepticons = new Decepticons().megatron();
        assertEquals(decepticons.getName(),subjectPicker.getName());
        assertEquals(decepticons.getRealName(),subjectPicker.getAlias());
    }
}