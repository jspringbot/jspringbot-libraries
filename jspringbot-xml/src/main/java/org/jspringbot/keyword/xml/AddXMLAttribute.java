package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Add XML Attribute",
        parameters = {"name", "value"},
        description = "classpath:desc/AddXMLAttribute.txt"
)
public class AddXMLAttribute extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        String name = String.valueOf(params[0]);
        String value = String.valueOf(params[1]);

        try {
            builderHelper.addAttribute(name, value);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error adding attribute name=%s, value=%s.", name, value));
        }

        return null;
    }
}
