package org.jspringbot.keyword.selenium;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Add Cookie",
        parameters = {"cookieName", "cookieValue", "host", "domain", "path"},
        description = "classpath:desc/AddCookie.txt"
        )
public class AddCookie extends AbstractSeleniumKeyword {

    @Override
    public Object execute(Object[] params) {
        helper.addCookie(String.valueOf(params[0]),String.valueOf(params[1]),String.valueOf(params[2]),String.valueOf(params[3]),String.valueOf(params[4]));

        return null;
    }
}

