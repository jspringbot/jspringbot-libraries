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

package org.jspringbot.keyword.xml;

import com.jamesmurty.utils.XMLBuilder;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.w3c.dom.Element;

import java.util.Arrays;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class XMLHelperTest {
    @Autowired
    private XMLHelper helper;

    @Autowired
    private XMLBuilderHelper builderHelper;

    @Autowired
    private SampleResources resources;

    @Autowired
    private GetXMLXPathElementTextContent getXPathElementTextContentKeyword;

    @Test
    public void testGetXpathElements2() throws Exception {
        helper.setXmlString(resources.getSample2XMLString());

        List<Element> els = helper.getXpathElements("//balancesResponse");

        assertTrue("Should not be empty.", CollectionUtils.isNotEmpty(els));
    }

    @Test
    public void testGetXpathElements() throws Exception {
        helper.setXmlString(resources.getSampleXMLString());

        List<Element> els = helper.getXpathElements("//catalog/book");

        final List<String> expectedComponents = Arrays.asList("Gambardella, Matthew", "Ralls, Kim", "Corets, Eva");
        for(Element el : els) {
            String compName = (String) getXPathElementTextContentKeyword.execute(new Object[]{el, "//book/author"});
            assertTrue(expectedComponents.contains(compName));
        }
    }

    @Test
    public void testComplicatedXpath() throws Exception {
        helper.setXmlString(resources.getSampleXMLString());

        List<Element> els = helper.getXpathElements("//catalog/book/author[text()='Gambardella, Matthew']/../genre[text()='Computer']/..");
        assertEquals(1, els.size());
    }

    @Test
    public void testXpathMatchCount() throws Exception {
        helper.setXmlString(resources.getSampleXMLString());

        assertEquals(2, helper.getXpathMatchCount("//catalog/book/price[text()='5.95']/.."));
    }

    @Test
    public void textXMLBuilder() throws Exception {
        builderHelper.startNode("root", true);
        builderHelper.startNode("node");
        builderHelper.addAttribute("name", "value");
        builderHelper.endNode();
        builderHelper.endNode();

        assertEquals(builderHelper.asString(), XMLBuilder.create("root").e("node").a("name", "value").asString());
    }
}
