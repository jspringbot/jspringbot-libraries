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
        name = "Is Alert Present",
        description = "classpath:desc/IsAlertPresent.txt"
)
public class IsAlertPresent extends AbstractSeleniumKeyword {
    @Override
    public Boolean execute(Object[] params) {
        return helper.isAlertPresent();
    }
}