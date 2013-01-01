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

package org.jspringbot.keyword.i18n;

import org.jspringbot.keyword.expression.ExpressionHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-i18n-test.xml"})
public class I18nHelperTest {

    @Autowired
    private I18nHelper helper;

    @Autowired
    protected ExpressionHelper expressionHelper;

    @Test
    public void testDefaultMessage() throws Exception {
        // This test only work if there is no english resource,
        // Otherwise it will default to english.
        // assertEquals("default message", helper.getMessage("message"));
    }

    @Test
    public void testNonExistingLocale() throws Exception {
        helper.setLocale("ko");
        assertEquals("english message", helper.getMessage("message"));
    }

    @Test
    public void testEnglishMessage() throws Exception {
        helper.setLocale("en");
        assertEquals("english message", helper.getMessage("message"));

        I18nObject dictionary = helper.createI18nObject();

        assertEquals("English Login Success", dictionary.get("login.success"));
        assertEquals("English Login Failure", dictionary.get("login.failure"));
    }

    @Test
    public void testChineseMessage() throws Exception {
        helper.setLocale("zh");
        assertEquals("chinese message", helper.getMessage("message"));

        I18nObject dictionary = helper.createI18nObject();

        assertEquals("Chinese Login Success", dictionary.get("login.success"));
        assertEquals("Chinese Login Failure", dictionary.get("login.failure"));
    }

    @Test
    public void testJapaneseMessage() throws Exception {
        helper.setLocale("ja");
        assertEquals("japanese message", helper.getMessage("message"));

        I18nObject dictionary = helper.createI18nObject();

        assertEquals("Japanese Login Success", dictionary.get("login.success"));
        assertEquals("Japanese Login Failure", dictionary.get("login.failure"));
    }

    @Test
    public void testExpressionSupport() throws Exception {
        helper.setLocale("ja_JP");

        expressionHelper.evaluationShouldBe("$[i18n:locale:language]", "ja");
        expressionHelper.evaluationShouldBe("$[i18n:locale:country]", "JP");
        expressionHelper.evaluationShouldBe("$[i18n:locale:displayCountry]", "Japan");
        expressionHelper.evaluationShouldBe("$[i18n:locale:displayLanguage]", "Japanese");

        expressionHelper.evaluationShouldBe("$[i18n:login.success]", "Japanese Login Success");
        expressionHelper.evaluationShouldBe("$[i18n:login.failure]", "Japanese Login Failure");
        expressionHelper.evaluationShouldBe("$[i18n:en:login.success]", "English Login Success");
        expressionHelper.evaluationShouldBe("$[i18n:en:login.failure]", "English Login Failure");
        expressionHelper.evaluationShouldBe("$[i18n:zh:login.success]", "Chinese Login Success");
        expressionHelper.evaluationShouldBe("$[i18n:zh:login.failure]", "Chinese Login Failure");

        // ensure that locale was not changed
        expressionHelper.evaluationShouldBe("$[i18n:login.success]", "Japanese Login Success");
        expressionHelper.evaluationShouldBe("$[i18n:login.failure]", "Japanese Login Failure");
    }
}
