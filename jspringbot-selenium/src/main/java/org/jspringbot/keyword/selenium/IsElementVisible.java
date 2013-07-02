package org.jspringbot.keyword.selenium;


import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Is Element Visible",
        parameters = {"locator"},
        description = "classpath:desc/IsElementVisible.txt"
)
public class IsElementVisible extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.isElementVisible(String.valueOf(params[0]));
    }
}
