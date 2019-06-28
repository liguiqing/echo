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

package org.echo.messaging;


import java.io.Serializable;

/**
 * <pre>
 * 发布消息
 * </pre>
 *
 * @author liguiqing
 * @date 2019-06-14 10:22
 * @since V1.0.0
 * @param <T> the type of the publish message
 **/
public interface MessagePublish<T extends Serializable> {

   void publish(String topic, T message);
}
