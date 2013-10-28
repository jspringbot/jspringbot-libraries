package org.jspringbot.keyword.selenium.web;


import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
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
