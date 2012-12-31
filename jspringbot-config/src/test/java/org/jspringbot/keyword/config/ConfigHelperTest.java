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


import org.jspringbot.keyword.expression.ExpressionHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link org.jspringbot.keyword.config.ConfigHelper} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:sample-config.xml"})
public class ConfigHelperTest {

    @Autowired
    protected ConfigHelper configHelper;

    @Autowired
    protected ExpressionHelper expressionHelper;

    @Test
    public void testSample() throws Exception {
        configHelper.selectDomain("sample");

        assertEquals("value", configHelper.getProperty("sample"));
    }

    @Test
    public void testConfigExpression() throws Exception {
        configHelper.selectDomain("sample");
        assertEquals("value", expressionHelper.evaluate("#{config:sample}"));
        assertEquals("property value", expressionHelper.evaluate("#{config:sample:property}"));
        assertEquals("http://someurl.com", expressionHelper.evaluate("#{config:robot-variables:url}"));

        assertEquals(true, expressionHelper.evaluate("#{config:sample:booleanProperty:boolean}"));
        assertEquals(100, expressionHelper.evaluate("#{config:robot-variables:integerProperty:integer}"));

        assertEquals(true, expressionHelper.evaluate("#{b:config:sample:booleanProperty}"));

        expressionHelper.evaluationShouldBe("#{config:robot-variables:integerProperty:long}", "#{100}");
    }


}
