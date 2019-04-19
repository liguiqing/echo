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

package org.echo.exception;


import org.springframework.util.StringUtils;

/**
 * <p>
 * 业务异常超类，所有业务处理过程中产生的异常都必须为此类的子类
 * </P>
 *
 * @author liguiqing
 * @date 2019-04-18 09:30
 * @since V1.0.0
 **/
public class BusinessException extends RuntimeException{
    private String code;

    public BusinessException(String code, String message) {
        super(message);
        this.code = StringUtils.isEmpty(code) ?"No":code;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String getMessage(){
        return String.format("%s:%s",this.code,super.getMessage());
    }
}
