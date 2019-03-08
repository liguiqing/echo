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


import lombok.extern.slf4j.Slf4j;

import java.util.Date;

/**
 * <p>
 *
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-08 19:41
 * @since V1.0.0
 **/
@Slf4j
public class ClassUtilsTestBean2 {

    private ClassUtilsTestBean2() {
        log.debug(" private Constructor ClassUtilsTestBean2");
    }

    private ClassUtilsTestBean2(Date date) {
        log.debug(" private Constructor ClassUtilsTestBean2[Date]");
    }

    public ClassUtilsTestBean2(String s){
        log.debug(" public Constructor ClassUtilsTestBean2[String]");
    }
    public ClassUtilsTestBean2(String s,Long l){
        log.debug(" public Constructor ClassUtilsTestBean2[String,Long]");
    }

    public ClassUtilsTestBean2(String s,Date date) throws IllegalAccessException{
        log.debug(" public Constructor ClassUtilsTestBean2[String,Long]");
        throw new IllegalAccessException();
    }
}
