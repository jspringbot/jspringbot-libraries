package org.jspringbot.keyword.selenium.web;


import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
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
