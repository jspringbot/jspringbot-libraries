package org.jspringbot.keyword.selenium.web;


import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Is Element Selected",
        parameters = {"locator"},
        description = "classpath:desc/IsElementSelected.txt"
)
public class IsElementSelected extends AbstractSeleniumKeyword {

    public Object execute(Object[] params) {
        return helper.isElementSelected(String.valueOf(params[0]));
    }
}
