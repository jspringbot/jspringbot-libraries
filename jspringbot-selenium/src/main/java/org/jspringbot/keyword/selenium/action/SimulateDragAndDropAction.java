package org.jspringbot.keyword.selenium.action;

import org.jspringbot.keyword.selenium.SeleniumHelper;
import org.openqa.selenium.interactions.Action;

import java.io.IOException;

public class SimulateDragAndDropAction implements Action {

    private SeleniumHelper helper;

    private String srcLocator;

    private String destLocator;

    public SimulateDragAndDropAction(SeleniumHelper helper, String srcLocator, String destLocator) {
        this.helper = helper;
        this.srcLocator = srcLocator;
        this.destLocator = destLocator;
    }

    @Override
    public void perform() {
        try {
            helper.simulateDragAndDrop(srcLocator, destLocator);
        } catch (IOException e) {
            throw new IllegalStateException(e.getMessage(), e);
        }
    }
}
