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

package org.jspringbot.keyword.date;

import junitx.util.PrivateAccessor;
import org.jspringbot.keyword.expression.ExpressionHelper;
import org.jspringbot.spring.ApplicationContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;

import static junit.framework.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class DateHelperTest {

    @Autowired
    private DateHelper helper;

    @Autowired
    protected ExpressionHelper expressionHelper;

    @Autowired
    private ApplicationContext context;

    @Test
    public void testPrint() throws Exception {
        assertNotNull(helper.formatDateTime());
    }

    @Test
    public void testExpression() throws Exception {
        helper.setDateTimeFormat("yyyy-MM-dd HH:mm:ss");
        expressionHelper.evaluate("$[date:parse('2013-02-08', 'yyyy-MM-dd')]");
        expressionHelper.evaluationShouldBe("$[date:current('+1d +1y -1M')]", "2014-01-09 00:00:00");
        assertTrue(Date.class.isInstance(expressionHelper.evaluate("$[date:toSQLDate()]")));
        assertTrue(Time.class.isInstance(expressionHelper.evaluate("$[date:toSQLTime()]")));
        assertTrue(Timestamp.class.isInstance(expressionHelper.evaluate("$[date:toSQLTimestamp()]")));

        assertTrue(Date.class.isInstance(expressionHelper.evaluate("$[date:toSQLDate('2013-03-08', 'yyyy-MM-dd')]")));
        assertTrue(Time.class.isInstance(expressionHelper.evaluate("$[date:toSQLTime()]")));
        assertTrue(Timestamp.class.isInstance(expressionHelper.evaluate("$[date:toSQLTimestamp()]")));

        System.out.println(expressionHelper.evaluate("$[date:toSQLDate()]"));
        System.out.println(expressionHelper.evaluate("$[date:toSQLTime()]"));
        System.out.println(expressionHelper.evaluate("$[date:toSQLTimestamp()]"));

        System.out.println(expressionHelper.evaluate("$[date:firstDayOfMonth()]"));
        System.out.println(expressionHelper.evaluate("$[date:lastDayOfMonth()]"));
        System.out.println(expressionHelper.evaluate("$[date:firstDayOfYear()]"));

    }

    @Test
    public void testParseDate() throws Exception {
        System.out.println(expressionHelper.evaluate("$[date:changeISODateFormat('2013-03-18T15:39:43.000+08:00','yyyy-MM-dd HH:mm:ss')]"));
    }

    @Test
    public void testName() throws Exception {
        expressionHelper.evaluate("$[date:isoParse('2013-03-18T15:39:43.000+08:00')]");
        expressionHelper.evaluationShouldBeTrue("$[date:isBeforeNow()]");
    }

    @Before
    public void setUp() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "set", new Class[]{ApplicationContext.class}, new Object[]{context});
    }

    @After
    public void tearDown() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "remove", new Class[]{}, new Object[]{});
    }
}
