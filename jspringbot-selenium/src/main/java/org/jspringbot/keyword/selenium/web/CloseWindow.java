package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Close Window",
        parameters = {"url"},
        description = "classpath:desc/CloseWindow.txt"
)
public class CloseWindow extends AbstractSeleniumKeyword{

	@Override
	public Object execute(Object[] params) throws Exception {
		
		helper.closeWindow(String.valueOf(params[0]));
		
		return null;
	}

}
