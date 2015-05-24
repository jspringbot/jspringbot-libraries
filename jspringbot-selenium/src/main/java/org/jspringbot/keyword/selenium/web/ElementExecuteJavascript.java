package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(
        name = "Element Execute Javascript",
        parameters = {"locator", "code", "*arguments"},
        description = "classpath:desc/ElementExecuteJavascript.txt"
)
public class ElementExecuteJavascript  extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) throws IOException {
        if(params.length > 2) {
            Object[] args = new Object[params.length - 2];
            System.arraycopy(params, 2, args, 0, params.length - 2);

            return helper.executeJavascript(String.valueOf(params[0]), String.valueOf(params[1]), args);
        } else {
            return helper.executeJavascript(String.valueOf(params[0]), String.valueOf(params[1]));
        }
    }
}
