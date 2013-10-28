package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Maximize Window",
        description = "classpath:desc/MaximizeWindow.txt"
)
public class MaximizeWindow extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        helper.windowMaximize();


        return null;
    }
}
