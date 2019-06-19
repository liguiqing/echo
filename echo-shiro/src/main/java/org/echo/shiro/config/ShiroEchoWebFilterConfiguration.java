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

package org.echo.shiro.config;


import com.google.common.collect.Maps;
import org.apache.shiro.spring.config.web.autoconfigure.ShiroWebFilterConfiguration;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.spring.web.config.AbstractShiroWebFilterConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.Map;
import java.util.Optional;

/**
 * Descriptions:
 * <pre>
 * ShiroEchoWebFilterConfiguration
 * </Pre>
 * @author liguiqing
 * @since V1.0.0 2019-06-19 08:31
 **/
@Configuration
@ConditionalOnProperty(name = "shiro.web.enabled", matchIfMissing = true)
@AutoConfigureBefore(ShiroWebFilterConfiguration.class)
public class ShiroEchoWebFilterConfiguration extends AbstractShiroWebFilterConfiguration {

    @Bean
    ShiroFilterFactoryBean shiroFilterFactoryBean(Optional<Map<String, Filter>> filters) {
        var factoryBean =  super.shiroFilterFactoryBean();
        factoryBean.setFilters(filters.orElse(Maps.newHashMap()));
        return factoryBean;
    }
}
