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


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.util.Optional;

/**
 * <p>
 * 通过类的Field读取
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-04 09:12
 * @since V1.0.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class FieldSaltReader extends AbstractSaltReader{

    private String field = "salt";

    public FieldSaltReader(String field, Optional<SaltReader> other) {
        this.field = field;
        other.ifPresent(this::setOther);
    }

    @Override
    protected String doRead(Object o){
        try {
            return FieldUtils.readField(o,field,true).toString();
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
