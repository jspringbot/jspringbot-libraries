package org.jspringbot.keyword.selenium.action;

import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.openqa.selenium.interactions.Action;

public class WaitUntilElementVisibleAction implements Action {

    private SeleniumHelper helper;

    private String locator;

    public WaitUntilElementVisibleAction(SeleniumHelper helper, String locator) {
        this.helper = helper;
        this.locator = locator;
    }

    @Override
    public void perform() {
        helper.waitTillElementVisible(locator);
    }
}
