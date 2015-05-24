package org.jspringbot.keyword.selenium.action;

import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.openqa.selenium.interactions.Actions;

public class CustomActions extends Actions {
    private SeleniumHelper helper;

    public CustomActions(SeleniumHelper helper) {
        super(helper.getDriver());

        this.helper = helper;
    }

    public CustomActions waitUntilElementVisible(String locator) {
        action.addAction(new WaitUntilElementVisibleAction(helper, locator));
        return this;
    }

    public CustomActions waitUntilElementFound(String locator) {
        action.addAction(new WaitUntilElementFoundAction(helper, locator));
        return this;
    }

    public CustomActions simulateDragAndDrop(String srcLocator, String destLocator) {
        action.addAction(new SimulateDragAndDropAction(helper, srcLocator, destLocator));
        return this;
    }

}
