package org.jspringbot.keyword.selenium;


import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Is Text Present",
        parameters = {"text"},
        description = "classpath:desc/IsTextPresent.txt"
)
public class IsTextPresent extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.isTextPresent(String.valueOf(params[0]));
    }
}
