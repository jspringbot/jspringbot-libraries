package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Navigate To In New Window",
        parameters = {"url"},
        description = "classpath:desc/NavigateToInNewWindow.txt"
)
public class NavigateToInNewWindow extends AbstractSeleniumKeyword {

	@Override
	public Object execute(Object[] params) throws Exception {
		
		return helper.navigateToInNewWindow(String.valueOf(params[0]));
		
	}
	
}
