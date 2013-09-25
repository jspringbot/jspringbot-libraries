package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Set XML Text",
        parameters = {"text"},
        description = "classpath:desc/SetXMLText.txt"
)
public class SetXMLText extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        String text = String.valueOf(params[0]);

        try {
            builderHelper.setText(text);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error adding text name=%s.", text));
        }

        return null;
    }
}
