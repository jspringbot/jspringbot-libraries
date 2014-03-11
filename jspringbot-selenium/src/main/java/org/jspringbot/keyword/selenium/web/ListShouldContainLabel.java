package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.springframework.stereotype.Component;


@Component
@KeywordInfo(
        name = "List Should Contain Label",
		parameters = {"locator", "label"},
        description = "classpath:desc/ListShouldContainLabel.txt"
)
public class ListShouldContainLabel extends AbstractSeleniumKeyword{

	@Override
	public Object execute(Object[] params) throws Exception {
		helper.listShouldContainLabel(String.valueOf(params[0]), String.valueOf(params[1]));
		return null;
	}
	
}
