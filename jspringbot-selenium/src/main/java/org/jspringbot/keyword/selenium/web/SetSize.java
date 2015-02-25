package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Set Size",
        parameters = {"width","height"},
        description = "classpath:desc/SetSize.txt"
        )
public class SetSize extends AbstractSeleniumKeyword {

    @Override
    public Object execute(Object[] params) throws Exception{
        helper.setSize(Integer.valueOf(String.valueOf(params[0])),Integer.valueOf(String.valueOf(params[1])));

        return null;
    }
}

