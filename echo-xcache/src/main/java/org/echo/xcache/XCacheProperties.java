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

package org.echo.xcache;


import com.google.common.collect.Maps;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * <pre>
 * 缓存配置
 * </pre>
 *
 * @author liguiqing
 * @since V1.0.0 2019-06-15 15:47
 **/
@NoArgsConstructor
@Getter
@Setter
@ConfigurationProperties(prefix = "echo.xcache")
public class XCacheProperties {

    /** 是否存储空值，默认true，防止缓存穿透*/
    private boolean cacheNullValues = true;

    /** 全局过期时间，单位毫秒，默认0,永不过期*/
    private long defaultExpiration = 0;

    /** cacheName的过期时间，单位毫秒，优先级比defaultExpiration高*/
    private Map<String, Long> expires = Maps.newHashMap();

    /** 缓存key的前缀*/
    private String cachePrefix = "echo";

    /** 是否动态根据cacheName创建Cache的实现，默认true*/
    private boolean dynamic = true;

    /**是否启动二级缓存,默认值不启用.启用时必须配置redis服务**/
    private boolean level2Enabled = false;

    /** 缓存消息发布/订阅主题 **/
    private String cacheMessageTopic = "echo:binaryCache:topic";

    public String getCacheName(String name){
        if(name.startsWith(this.getCachePrefix().concat(":")))
            return name;
        return (StringUtils.isEmpty(this.getCachePrefix())?"echo":this.getCachePrefix()).concat(":").concat(name);
    }

    public long getTtl(String name,long defaultTtl){
        if(this.expires.keySet().contains(name))
            return this.expires.get(name);
        return defaultTtl;
    }
}
