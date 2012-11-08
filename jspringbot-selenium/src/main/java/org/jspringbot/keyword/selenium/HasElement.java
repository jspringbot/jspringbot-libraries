package org.jspringbot.keyword.selenium;


import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "HasElement", description = "Determines whether the given element exists.", parameters={"locator"})
public class HasElement extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.hasElement(String.valueOf(params[0]));
    }
}
