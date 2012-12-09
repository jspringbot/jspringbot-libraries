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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-i18n.xml"})
public class I18nHelperTest {

    @Autowired
    private I18nHelper helper;

    @Test
    public void testDefaultMessage() throws Exception {
        // This test only work if there is no english resource,
        // Otherwise it will default to english.
        // assertEquals("default message", helper.getMessage("message"));
    }

    @Test
    public void testNonExistingLocale() throws Exception {
        helper.setLanguage("ko");
        assertEquals("english message", helper.getMessage("message"));
    }

    @Test
    public void testEnglishMessage() throws Exception {
        helper.setLanguage("en");
        assertEquals("english message", helper.getMessage("message"));

        Map<String, String> map = helper.createDictionary("login");

        assertEquals("English Login Success", map.get("login.success"));
        assertEquals("English Login Failure", map.get("login.failure"));
    }

    @Test
    public void testChineseMessage() throws Exception {
        helper.setLanguage("zh");
        assertEquals("chinese message", helper.getMessage("message"));

        Map<String, String> map = helper.createDictionary("login");

        assertEquals("Chinese Login Success", map.get("login.success"));
        assertEquals("Chinese Login Failure", map.get("login.failure"));
    }

    @Test
    public void testJapaneseMessage() throws Exception {
        helper.setLanguage("ja");
        assertEquals("japanese message", helper.getMessage("message"));

        Map<String, String> map = helper.createDictionary("login");

        assertEquals("Japanese Login Success", map.get("login.success"));
        assertEquals("Japanese Login Failure", map.get("login.failure"));

    }
}
