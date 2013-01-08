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

import org.apache.commons.collections.CollectionUtils;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.util.List;

@Component
@KeywordInfo(name = "Get XML XPath Element", description = "Get XML XPath Element.", parameters = {"xpathExpression"})
public class GetXMLXPathElement extends AbstractXMLKeyword{

    @Override
    public Object execute(Object[] params) {
        try {
            List<Element> elements = helper.getXpathElements(String.valueOf(params[0]));

            if(CollectionUtils.isEmpty(elements)) {
                throw new IllegalArgumentException(String.format("No Element fount for xpath expression '%s'.", params[0]));
            }

            return elements.iterator().next();
        } catch (TransformerException e) {
            throw new IllegalArgumentException(String.format("Error while getting element for xpath expression %s'.", params[0]));
        }
    }
}
