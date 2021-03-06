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

package org.echo.xcache.caffeine;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.echo.util.CollectionsUtil;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * CaffeineProperties Properties
 *
 * @author Liguiqing
 * @since V1.0
 */

@NoArgsConstructor
@Slf4j
@Getter
@Setter
@Accessors(chain = true)
@ConfigurationProperties(prefix = "echo.xcache.caffeine")
public class CaffeineCacheProperties {

    private CaffeineProperties defaultProp;

    private List<CaffeineProperties> cachesOnBoot;

    CaffeineProperties getProp(String name){
        if(CollectionsUtil.isNotNullAndNotEmpty(this.cachesOnBoot)){
            for(CaffeineProperties p:cachesOnBoot){
                if(p.getName().equalsIgnoreCase(name))
                    return p;
            }
        }

        return defaultProp;
    }
}