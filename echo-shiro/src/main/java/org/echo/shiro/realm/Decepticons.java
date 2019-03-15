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


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * <p>
 * 测试用户名:Megatron(威震天),用于系统登录测试，无权限
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-08 14:32
 * @since V1.0.0
 **/
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString(exclude = {"password","salt"})
public class Decepticons implements Serializable {
    private String name ;
    private String realName ;
    private String password;
    private String salt;

    public Decepticons megatron() {
        return new Decepticons("Megatron", "威震天", "malilihong","Galvatron");
    }
}
