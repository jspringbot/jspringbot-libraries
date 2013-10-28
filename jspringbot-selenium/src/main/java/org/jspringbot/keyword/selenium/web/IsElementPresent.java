package org.jspringbot.keyword.selenium.web;

/**
 * @author fcabuslay
 *
 */
import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Is Element Present",
        parameters = {"locator"},
        description = "classpath:desc/IsElementPresent.txt"
)

public class IsElementPresent extends AbstractSeleniumKeyword {
    
    @Override
    public Object execute(Object[] params) {
        return helper.findElement(String.valueOf(params[0]));
    }  

}


