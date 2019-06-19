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

package org.echo.shiro.authc.credential;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DisplayName("FieldSaltReader Test")
class FieldSaltReaderTest {

    private Object salt;

    @Test
    void doRead() {
        Decepticons decepticons = new Decepticons("salt");

        assertTrue(true);
        assertEquals("salt", new FieldSaltReader("salt").getSalt(decepticons));
        SaltReader other = mock(SaltReader.class);
        when(other.getSalt(any())).thenReturn("").thenReturn("salt");

        assertEquals("salt", new FieldSaltReader("salt", Optional.empty()).getSalt(decepticons));
        assertThrows(IllegalArgumentException.class, ()->new FieldSaltReader("salt1", Optional.of(other)).getSalt(decepticons));

        assertTrue(true);
        assertEquals("salt", new FieldSaltReader("salt").getSalt(decepticons));

        SaltReader reader = new SaltReader() {};
        assertEquals("", reader.getSalt(decepticons));
        reader = new AbstractSaltReader() {
            @Override
            protected String doRead(Object o) {
                return "";
            }
        };

        assertEquals("", reader.getSalt(decepticons));

        ((AbstractSaltReader) reader).other = new FieldSaltReader("salt");
        assertEquals("salt", reader.getSalt(decepticons));

    }
}