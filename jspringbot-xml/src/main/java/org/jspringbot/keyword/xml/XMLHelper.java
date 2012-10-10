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

import com.sun.org.apache.xerces.internal.parsers.DOMParser;
import com.sun.org.apache.xpath.internal.XPathAPI;
import org.apache.commons.lang.StringUtils;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.jspringbot.syntax.HighlighterUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class XMLHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(XMLHelper.class);

    protected String xmlString;

    protected Document document;

    public void setXmlString(String xmlString) throws IOException, SAXException {
        this.xmlString = xmlString;

        DOMParser parser = new DOMParser();
        parser.parse(new InputSource(new StringReader(xmlString)));

        LOG.createAppender()
            .appendBold("XML String:")
            .appendXML(XMLFormatter.prettyPrint(xmlString))
            .log();

        this.document = parser.getDocument();
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    /**
     * XPath Text Content Should be Equal
     */
    public void xpathTextContentShouldBeEqual (String xpathExpression, String expectedValue) throws TransformerException {
        String value = getXPathSingleTextContent(xpathExpression);

        LOG.createAppender()
            .appendBold("Xpath Text Content Should be Equal:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Expected Value", expectedValue)
            .appendProperty("Actual Value", value)
            .log();

        if (!StringUtils.equals(value, expectedValue)) {
            throw new IllegalArgumentException(String.format("Expecting text content '%s' but was '%s'", expectedValue, value));
        }
    }

    public int getXpathMatchCount(String xpathExpression) throws TransformerException {

        int nodeLength = getNodeList(xpathExpression).getLength();

        LOG.createAppender()
            .appendBold("Get Xpath Match Count:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Actual Found", nodeLength)
            .log();

        return nodeLength;
    }

    public void xpathShouldMatchXTimes (String xpathExpression, int numberOfTimes) throws TransformerException {
        int nodeLength = getNodeList(xpathExpression).getLength();

        LOG.createAppender()
            .appendBold("Xpath Should Match X Times:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Expected Number of Times", numberOfTimes)
            .appendProperty("Actual Number of Times", nodeLength)
            .log();

        if (nodeLength != numberOfTimes) {
            throw new IllegalArgumentException(String.format("Expecting '%d' occurrences of '%s' expression but was '%d'", numberOfTimes, xpathExpression, nodeLength));
        }
    }

    /**
     * Get Xpath Single Text Contents
     */
    public String getXPathSingleTextContent(String xpathExpression) throws TransformerException {
        Element element = (Element) XPathAPI.selectSingleNode(document, xpathExpression);

        if (element == null) {
            throw new IllegalArgumentException(String.format("Xpath Expression '%s' not found.", xpathExpression));
        }

        LOG.createAppender()
            .appendBold("Get Xpath Single Text Content:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Text Content", element.getTextContent())
            .log();

        return element.getTextContent();
    }

    /**
     * Get Xpath Text Contents
     */
    public List<String> getXpathTextContents(String xpathExpression) throws TransformerException {
        NodeList nodeList = getNodeList(xpathExpression);
        if (nodeList.getLength() == 0) {
            return Collections.emptyList();
        }

        List<String> list = new ArrayList<String>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            list.add(element.getTextContent());
        }

        LOG.createAppender()
            .appendBold("Get Xpath Text Contents:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Text Contents", String.valueOf(list))
            .log();

        return list;
    }

    /**
     * Get Xpath Text Contents
     */
    public List<Element> getXpathElements(String xpathExpression) throws TransformerException {
        NodeList nodeList = getNodeList(xpathExpression);
        if (nodeList.getLength() == 0) {
            return Collections.emptyList();
        }

        StringBuilder buf = new StringBuilder();
        List<Element> list = new ArrayList<Element>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            list.add(element);
            buf.append("<b>Element [").append(i).append("]:</b>").append(HighlighterUtils.INSTANCE.highlightXML(XMLFormatter.prettyPrint(element)));
        }

        LOG.pureHtml(buf.toString());

        return list;
    }

    private NodeList getNodeList(String xpathExpression) throws TransformerException {
        NodeList nodeList = XPathAPI.selectNodeList(document, xpathExpression);
        if (nodeList == null) {
            throw new IllegalArgumentException(String.format("Xpath Expression '%s' not found.", xpathExpression));
        }

        LOG.createAppender()
            .appendBold("Get Node List:")
            .appendProperty("Xpath Expression", xpathExpression)
            .appendProperty("Number of Elements Found", nodeList.getLength())
            .log();

        return nodeList;
    }

}
