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

package org.echo.ddd.domain.entity;

import org.echo.ddd.domain.id.Identity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("EntityObject Test")
class EntityObjectTest {

    @Test
    void test(){
        assertTrue(Boolean.TRUE);
        EntityObject eo = new EntityObject(){
            @Override
            public Identity getId() {
                return new Identity() {
                    @Override
                    public Serializable getId() {
                        return 0L;
                    }

                    @Override
                    public void setId(Serializable id) {

                    }
                };
            }

        };
        assertEquals(0L,eo.getId().getId());
    }
}