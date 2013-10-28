package org.jspringbot.keyword.selenium.web;


import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Is Text Present In Page Source",
        parameters = {"html", "text"},
        description = "classpath:desc/IsTextPresentInPageSource.txt"
)
public class IsTextPresentInPageSource extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.isTextPresentInPageSource(String.valueOf(params[0]), String.valueOf(params[1]));
    }
}
