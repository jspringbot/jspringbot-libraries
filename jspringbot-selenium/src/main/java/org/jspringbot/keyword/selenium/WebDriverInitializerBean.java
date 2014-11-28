package org.jspringbot.keyword.selenium;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Required;

public class WebDriverInitializerBean implements InitializingBean {

    private boolean maximize = false;

    protected SeleniumHelper helper;

    public WebDriverInitializerBean(SeleniumHelper helper) {
        this.helper = helper;
    }

    @Required
    public void setMaximize(boolean maximize) {
        this.maximize = maximize;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(maximize) {
            helper.windowMaximize();
        }
    }
}
