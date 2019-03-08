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

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@DisplayName("Echo : NumbersUtil Test")
class SpringsTest {

    @Mock
    ApplicationContext applicationContext;

    @Mock
    DefaultListableBeanFactory beanFactory;

    @BeforeEach
    public void before(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void test(){
        Springs springs = new Springs();
        springs.setApplicationContext(applicationContext);
        when(applicationContext.getBean(anyString())).thenReturn(new Object());
        when(applicationContext.getBean(any(Class.class))).thenReturn(new Object());
        when(applicationContext.getAutowireCapableBeanFactory()).thenReturn(beanFactory);
        doNothing().when(beanFactory).registerBeanDefinition(anyString(), any(BeanDefinition.class));
        assertNotNull(Springs.getBean(""));
        assertNotNull(Springs.getBean(String.class));

        Springs.registerBean("a","a",null);
        HashMap<String,Object> pvs = new HashMap<>();
        Springs.registerBean("a","a",pvs);
        pvs.put("a", "a");
        Springs.registerBean("a","a",pvs);
        Springs.removeBean("a");
    }
}