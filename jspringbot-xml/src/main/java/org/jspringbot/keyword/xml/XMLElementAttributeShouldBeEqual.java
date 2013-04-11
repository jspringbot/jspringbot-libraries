package org.jspringbot.keyword.xml;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.stereotype.Component;
import org.w3c.dom.Element;

@Component
@KeywordInfo(
        name = "XML Element Attribute Should Be Equal",
        parameters = {"element", "attribute", "expectedValue"},
        description = "classpath:desc/XMLElementAttributeShouldBeEqual.txt"
)
public class XMLElementAttributeShouldBeEqual extends AbstractXMLKeyword {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(XMLElementAttributeShouldBeEqual.class);

    @Override
    public Object execute(Object[] params) {
        Element element = (Element) params[0];
        String attributeName = String.valueOf(params[1]);
        String expectedValue = String.valueOf(params[2]);

        String result = element.getAttribute(attributeName);

        LOG.createAppender()
                .appendBold("Get Element Attribute:")
                .appendProperty("Attribute", attributeName)
                .appendProperty("Actual", result)
                .appendProperty("Expected", expectedValue)
                .log();

        if(!StringUtils.equals(result, expectedValue)) {
            throw new IllegalArgumentException("Attribute name and expected value did not match.");
        }

        return null;
    }
}
