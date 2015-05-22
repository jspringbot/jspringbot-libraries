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

package org.jspringbot.syntax;

import org.junit.Test;

/**
 * Test for syntax highlighter
 */
public class HighlighterUtilsTest {
    @Test
    public void testXML() throws Exception {
        HighlighterUtils.INSTANCE.setEnable(true);
        System.out.println(HighlighterUtils.INSTANCE.highlightText("Access-Control-Allow-Credentials = \"true\""));
        System.out.println(HighlighterUtils.INSTANCE.highlightSQL("select * from table where h=5"));
    }

    @Test
    public void testJSON() throws Exception {
        HighlighterUtils.INSTANCE.setEnable(true);
        System.out.println(HighlighterUtils.INSTANCE.highlightJSON("{\"a\": \"true\"}"));
    }

    @Test
    public void testCSS() throws Exception {
        HighlighterUtils.INSTANCE.setEnable(true);
        System.out.println(HighlighterUtils.INSTANCE.highlightCss("css=#alvin a.hello"));
    }
}
