package org.jspringbot.keyword.selenium;

/**
 * @author fcabuslay
 *
 */
import org.jspringbot.KeywordInfo;
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


