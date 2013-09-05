package org.jspringbot.keyword.selenium;


import org.jspringbot.KeywordInfo;
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
