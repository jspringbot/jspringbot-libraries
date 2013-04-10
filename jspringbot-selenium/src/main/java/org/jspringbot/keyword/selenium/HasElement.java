package org.jspringbot.keyword.selenium;


import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "HasElement",
        parameters = {"locator"},
        description = "classpath:desc/HasElement.txt"
)
public class HasElement extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.hasElement(String.valueOf(params[0]));
    }
}
