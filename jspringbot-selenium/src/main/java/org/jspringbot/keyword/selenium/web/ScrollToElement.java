package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Scroll To Element",
        parameters = {"locator"},
        description = "classpath:desc/ScrollToElement.txt"
        )
public class ScrollToElement extends AbstractSeleniumKeyword {

    @Override
    public Object execute(Object[] params) throws Exception{
        helper.scrollToElement(String.valueOf(params[0]));

        return null;
    }
}

