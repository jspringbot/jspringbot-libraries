package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "XML Reset",
        description = "classpath:desc/XMLReset.txt"
)
public class XMLReset  extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        try {
            builderHelper.reset();
            helper.reset();

            return null;
        } catch (Exception e) {
            throw new IllegalArgumentException("Error creating xml string.");
        }
    }
}