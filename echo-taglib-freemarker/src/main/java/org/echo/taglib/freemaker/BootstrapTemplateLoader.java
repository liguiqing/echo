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

package org.echo.taglib.freemaker;

import freemarker.cache.ClassTemplateLoader;
import freemarker.cache.TemplateLoader;
import lombok.experimental.Delegate;

/**
 * <p>
 * Bootstrap4 标签库加载器
 * </P>
 *
 * @author liguiqing
 * @date 2019-03-11 10:38
 * @since V1.0.0
 **/
public class BootstrapTemplateLoader implements TemplateLoader {

    @Delegate
    private final TemplateLoader templateLoader = new ClassTemplateLoader(this.getClass(),"/bootstrap");

}
