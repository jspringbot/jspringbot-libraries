package org.jspringbot.keyword.http;

import org.springframework.beans.factory.annotation.Autowired;

public class UserAgentBean {

	@Autowired
	protected HTTPHelper helper;

	public UserAgentBean(HTTPHelper helper) {
		this.helper = helper;
	}
	public void setUserAgentFlag(boolean userAgentFlag) {
		helper.setUserAgentFlag(userAgentFlag);
	}
	
	public void setUserAgentString(String userAgentString) {
		helper.setUserAgentString(userAgentString);
	}
    
}
