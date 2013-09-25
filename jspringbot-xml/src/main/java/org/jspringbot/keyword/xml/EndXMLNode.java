package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "End XML Node",
        description = "classpath:desc/XMLEndNode.txt"
)
public class EndXMLNode extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        try {
            builderHelper.endNode();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error ending xml node.");
        }

        return null;
    }
}
