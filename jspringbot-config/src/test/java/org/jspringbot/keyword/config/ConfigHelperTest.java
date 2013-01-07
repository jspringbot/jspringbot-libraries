/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.config;


import junitx.util.PrivateAccessor;
import org.jspringbot.keyword.expression.ExpressionHelper;
import org.jspringbot.keyword.expression.plugin.DefaultVariableProviderImpl;
import org.jspringbot.spring.ApplicationContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link org.jspringbot.keyword.config.ConfigHelper} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class ConfigHelperTest {

    @Autowired
    protected ConfigHelper configHelper;

    @Autowired
    protected ExpressionHelper expressionHelper;

    @Resource
    protected DefaultVariableProviderImpl defaultVariableProvider;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testConfigDir() throws Exception {
        configHelper.selectDomain("a");
        assertEquals("a value", configHelper.getProperty("a"));
        configHelper.selectDomain("b");
        assertEquals("b value", configHelper.getProperty("b"));
    }

    @Test
    public void testSample() throws Exception {
        configHelper.selectDomain("sample");

        assertEquals("value", configHelper.getProperty("sample"));
    }

    @Test
    public void testConfigExpression() throws Exception {
        configHelper.selectDomain("sample");
        assertEquals("value", expressionHelper.evaluate("$[config:sample]"));
        assertEquals("property value", expressionHelper.evaluate("$[config:sample:property]"));
        assertEquals("http://someurl.com", expressionHelper.evaluate("$[config:robot-variables:url]"));

        assertEquals(true, expressionHelper.evaluate("$[b:config:sample:booleanProperty]"));
        assertEquals(100, expressionHelper.evaluate("$[i:config:robot-variables:integerProperty]"));

        assertEquals(true, expressionHelper.evaluate("$[b:config:sample:booleanProperty]"));

        expressionHelper.evaluationShouldBe("$[l:config:robot-variables:integerProperty]", "$[100]");

    }

    @Test
    public void testConfigExpressionReplacement() throws Exception {
        defaultVariableProvider.add("var1", "var1Value");
        expressionHelper.evaluationShouldBe("$[config:b:var]", "var1Value/a");
    }

    @Before
    public void setUp() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "set", new Class[] {ApplicationContext.class}, new Object[] {context});
    }

    @After
    public void tearDown() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "remove", new Class[]{}, new Object[]{});
    }
}
