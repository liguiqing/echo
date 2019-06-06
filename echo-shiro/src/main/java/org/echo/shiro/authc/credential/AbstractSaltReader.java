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


import lombok.extern.slf4j.Slf4j;

/**
 * <p>
 * 密码Salt读取器
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-04 09:19
 * @since V1.0.0
 **/
@Slf4j
public abstract class AbstractSaltReader implements SaltReader{
    protected  SaltReader other;

    @Override
    public String getSalt(Object o) {
        return readFromOtherIfNecessary(doRead(o),o);
    }

    private String readFromOtherIfNecessary(String salt,Object o) {
        if(other != null && salt.length() == 0){
            return other.getSalt(o);
        }
        return salt;
    }

    protected void setOther(SaltReader other){
        this.other = other;
    }

    /**
     *
     * @param o Source of salt value Object
     * @return If read success return it else return ""
     */
    protected abstract String doRead(Object o);
}
