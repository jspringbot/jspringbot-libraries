package org.jspringbot.keyword.selenium.action;

import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.openqa.selenium.interactions.Action;

public class WaitUntilElementFoundAction implements Action {

    private SeleniumHelper helper;

    private String locator;

    public WaitUntilElementFoundAction(SeleniumHelper helper, String locator) {
        this.helper = helper;
        this.locator = locator;
    }

    @Override
    public void perform() {
        helper.waitTillElementFound(locator);
    }
}
