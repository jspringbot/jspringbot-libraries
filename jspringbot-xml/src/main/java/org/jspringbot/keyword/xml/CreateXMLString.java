package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Create XML String",
        description = "classpath:desc/CreateXMLString.txt"
)
public class CreateXMLString extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        try {
            return builderHelper.asString();
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating xml string.");
        }
    }
}
