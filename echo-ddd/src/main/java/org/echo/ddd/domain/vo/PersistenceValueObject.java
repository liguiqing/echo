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

package org.echo.ddd.domain.vo;


import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 * <p>
 * 完全被动持久化的值对象，多见于一对多，或者一对一单向关联
 * </P>
 *
 * @author liguiqing
 * @date 2019-04-12 07:44
 * @since V1.0.0
 **/
//@Getter
@MappedSuperclass
public abstract class PersistenceValueObject extends ValueObject{
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long tid;
}
