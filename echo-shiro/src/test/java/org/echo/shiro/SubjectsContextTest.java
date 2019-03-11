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

import com.google.common.collect.Sets;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.ThreadContext;
import org.echo.shiro.realm.Decepticons;
import org.echo.shiro.realm.PrimusSubjectPicker;
import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("SubjectsContextTest Test")
class SubjectsContextTest {

    Decepticons decepticons = new Decepticons().megatron();

    @BeforeEach
    public void before(){
        SecurityManager securityManager = mock(SecurityManager.class);
        ThreadContext.put(ThreadContext.SECURITY_MANAGER_KEY,securityManager);
        Subject subject = mock(Subject.class);
        when(subject.isAuthenticated()).thenReturn(true);
        when(subject.toString()).thenReturn("Mock Subject");
        PrincipalCollection principalCollection = mock(PrincipalCollection.class);
        when(subject.getPrincipals()).thenReturn(principalCollection);
        when(principalCollection.getPrimaryPrincipal()).thenReturn(null).thenReturn(decepticons).thenReturn(null).thenReturn(decepticons).thenReturn(decepticons).thenReturn(null);
        when(subject.getPrincipal()).thenReturn(null).thenReturn(decepticons).thenReturn(decepticons).thenReturn(decepticons);
        ThreadContext.put(ThreadContext.SUBJECT_KEY,subject);
        assertThrows(Exception.class,()->new PrivateConstructors().exec(Shiros.class));
    }

    @Test
    void lookup() {
        SubjectsContext subjectsContext = new SubjectsContext(Optional.empty());
        assertTrue(true);
        SubjectPicker picker = subjectsContext.lookup();
        assertEquals("",picker.getAlias());
        assertEquals("",picker.getName());
        assertFalse(picker.isAuthenticated());
        Set<SubjectPicker> pickers = Sets.newHashSet();
        pickers.add(new PrimusSubjectPicker());
        subjectsContext = new SubjectsContext(Optional.of(pickers));
        picker = subjectsContext.lookup();
        assertEquals(decepticons.getRealName(),picker.getAlias());
        assertEquals(decepticons.getName(),picker.getName());
        assertTrue(picker.isAuthenticated());

        picker = subjectsContext.lookup();
        assertEquals(decepticons.getRealName(),picker.getAlias());
        assertEquals(decepticons.getName(),picker.getName());
        assertTrue(picker.isAuthenticated());

        picker = subjectsContext.lookup();
        assertEquals(decepticons.getRealName(),picker.getAlias());
        assertEquals(decepticons.getName(),picker.getName());
        assertTrue(picker.isAuthenticated());


        pickers = Sets.newHashSet();
        pickers.add(new SubjectPicker(){});
        subjectsContext = new SubjectsContext(Optional.of(pickers));
        picker = subjectsContext.lookup();
        assertEquals("Mock Subject",picker.getAlias());
        assertEquals("Mock Subject",picker.getName());
        assertTrue(picker.isAuthenticated());

        pickers = Sets.newHashSet();
        pickers.add(new SubjectPicker(){});
        subjectsContext = new SubjectsContext(Optional.of(pickers));
        picker = subjectsContext.lookup();
        assertEquals("Mock Subject",picker.getAlias());
        assertEquals("Mock Subject",picker.getName());
        assertTrue(picker.isAuthenticated());
    }
}