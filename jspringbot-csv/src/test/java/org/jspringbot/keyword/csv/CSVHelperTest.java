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

package org.jspringbot.keyword.csv;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

/**
 * Test for {@link org.jspringbot.keyword.csv.CSVHelper} class.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:sample-csv.xml"})
public class CSVHelperTest {

    @Autowired
    private CSVHelper helper;

    @Test
    public void testParseString() throws Exception {
        helper.parseCSVString("test,sample,csv,file");

        assertEquals("number of lines", 1, helper.getLines().size());
        helper.createCriteria();
        helper.addColumnIndexEqualsRestriction(0, "test");
        assertEquals("column value check", "file", helper.firstResultColumnIndex(3));
    }

    @Test
    public void testGetColumnValuesWhereIndexIs() throws Exception {
        helper.parseCSVResource("classpath:sample.csv");

        assertEquals("number of lines", 10, helper.getLines().size());
        helper.createCriteria();
        helper.addColumnIndexEqualsRestriction(5, "test-session");

        assertEquals("column value check", "new", helper.firstResultColumnIndex(6));
    }

    @Test
    public void testCriteria() throws Exception {
        helper.parseCSVResource("classpath:sample.csv");

        assertEquals("number of lines", 10, helper.getLines().size());

        helper.createCriteria();
        helper.addColumnIndexEqualsRestriction(5, "test-session");

        assertEquals("column value check", "new", helper.list().get(0)[6]);
    }

    @Test
    public void testTestDisjunction() throws Exception {
        helper.parseCSVResource("classpath:sample.csv");

        assertEquals("number of lines", 10, helper.getLines().size());

        helper.createCriteria();
        helper.startDisjunction();
        helper.addColumnIndexEqualsRestriction(5, "test-session");
        helper.addColumnIndexEqualsRestriction(5, "_hod");
        helper.endDisjunction();

        assertEquals("column value check", 2, helper.list().size());
    }

    @Test
    public void testTestConjunction() throws Exception {
        helper.parseCSVResource("classpath:sample.csv");

        assertEquals("number of lines", 10, helper.getLines().size());

        helper.createCriteria();
        helper.startDisjunction();

        helper.startConjunction();
        helper.addColumnIndexEqualsRestriction(5, "test-session");
        helper.addColumnIndexEqualsRestriction(6, "new");
        helper.endConjunction();

        helper.startConjunction();
        helper.addColumnIndexEqualsRestriction(5, "_hod");
        helper.addColumnIndexEqualsRestriction(6, "19");
        helper.endConjunction();

        helper.endDisjunction();

        assertEquals("column value check", 2, helper.list().size());
    }
}
