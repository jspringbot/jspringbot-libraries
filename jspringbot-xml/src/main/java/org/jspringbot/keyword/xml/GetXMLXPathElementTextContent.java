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
import org.jspringbot.JSpringBotLogger;
import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlighterUtils;
import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.StringReader;

@Component
@KeywordInfo(name = "Get XML XPath Element Text Content", description = "Get XML XPath Element Text Content", parameters = {"element", "xpathExpression"})
public class GetXMLXPathElementTextContent extends AbstractXMLKeyword {

    public static final JSpringBotLogger LOG = JSpringBotLogger.getLogger(GetXMLXPathElementTextContent.class);

    @Override
    public Object execute(Object[] params) throws ParserConfigurationException, IOException, SAXException {
        try {
            String xmlString = XMLFormatter.prettyPrint((Element) params[0]);
            String xpathExpression = String.valueOf(params[1]);

            DOMParser parser = new DOMParser();
            parser.parse(new InputSource(new StringReader(xmlString)));

            Document document = parser.getDocument();

            Element result = (Element) XPathAPI.selectSingleNode(document, xpathExpression);

            if(result == null) {
                throw new IllegalStateException(String.format("No element found given the expression %s.", xpathExpression));
            }

            StringBuilder buf = new StringBuilder();
            buf.append(String.format("Xpath Expression = \"%s\"\n", xpathExpression));
            buf.append(String.format("Text Content = \"%s\"", result.getTextContent()));

            LOG.pureHtml("<b>Get XPath Element Text Content:</b>" + HighlighterUtils.INSTANCE.highlightText(buf.toString()));
            LOG.pureHtml("<b>Element XML String:</b>" + HighlighterUtils.INSTANCE.highlightXML(xmlString));

            return result.getTextContent();
        } catch (TransformerException e) {
            throw new IllegalArgumentException(String.format("Error while getting element for xpath expression %s'.", params[0]));
        }
    }
}
