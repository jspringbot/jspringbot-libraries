package org.jspringbot.keyword.selenium;

import org.springframework.beans.factory.annotation.Autowired;

public class OmnitureDebuggerBean {

	@Autowired
	protected OmnitureDebugger debugger;

	public OmnitureDebuggerBean(OmnitureDebugger debugger) {
		this.debugger = debugger;
	}

	public void setOmnitureDebuggerLocation(String location) {
		debugger.setOmnitureDebuggerLocation(location);
	}

	public void setOmnitureDebuggerWaitTimeInMillis(String time) {
		debugger.setOmnitureDebuggerWaitTimeInMillis(Integer.parseInt(time));
	}

}
