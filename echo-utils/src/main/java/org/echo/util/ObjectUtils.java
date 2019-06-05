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
import org.echo.exception.ThrowableToString;

import java.io.*;

/**
 * <p>
 * 对象工具
 * </P>
 *
 * @author liguiqing
 * @date 2019-06-05 13:30
 * @since V1.0.0
 **/
@Slf4j
public class ObjectUtils {
    private ObjectUtils() {
        throw new AssertionError("No org.echo.util.ObjectUtils instances for you!");
    }

    /**
     * 深度clone 被clone对象须实现 {@link Serializable}　
     * @param t   a object will be cloned
     * @return same type of input . if failure return null;
     * @param <T> type of element will be cloned
     */
    public static <T> T deepClone(T t) {
        T tt = null;
        try (var baos = new ByteArrayOutputStream();
             var oos = new ObjectOutputStream(baos)) {
            oos.writeObject(t);
            try (var bais = new ByteArrayInputStream(baos.toByteArray());
                 var ois = new ObjectInputStream(bais)) {
                tt =  (T) ois.readObject();
            }
        } catch (Exception e) {
            log.warn(ThrowableToString.toString(e));
        }
        return tt;
    }
}
