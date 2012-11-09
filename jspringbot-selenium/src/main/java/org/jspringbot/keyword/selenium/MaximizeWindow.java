package org.jspringbot.keyword.selenium;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Maximize Window", description = "Maximizes window")
public class MaximizeWindow extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        helper.windowMaximize();

        return null;
    }
}
