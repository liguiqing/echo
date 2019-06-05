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

package org.echo.util;

import org.echo.test.PrivateConstructors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("ObjectUtils Test")
class ObjectUtilsTest {

    @Test
    void deepClone() {
        String a  = "a";
        String b = ObjectUtils.deepClone(a);
        assertEquals(a,b);

        CloneTestBean c1 = new CloneTestBean();
        c1.s = "s";
        c1.i = 10;
        c1.l = 9L;
        c1.time = LocalDateTime.now().minusSeconds(10);

        CloneTestBean c2 = new CloneTestBean();
        c2.s = "2s";
        c2.i = 20;
        c2.l = 2L;
        c2.time = c1.time.minusSeconds(20);
        c2.parent = c1;

        CloneTestBean c3 = ObjectUtils.deepClone(c2);
        assertEquals(c2.s,c3.s);
        assertEquals(c1.s,c3.parent.s);

        ClassUtilsTestBean cu = ObjectUtils.deepClone(new ClassUtilsTestBean());
        assertNull(cu);

        assertThrows(Exception.class, ()->new PrivateConstructors().exec(ObjectUtils.class));
    }
}