package org.jspringbot.keyword.selenium.web;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class WebDriverWaitBean {

    private WebDriver driver;

    public WebDriverWaitBean(WebDriver driver) {
        this.driver = driver;
    }

    public void setImplicitWaitInSeconds(int wait) {
        driver.manage().timeouts().implicitlyWait(wait, TimeUnit.SECONDS);
    }

    public void setPageLoadWaitInSeconds(int wait) {
        driver.manage().timeouts().pageLoadTimeout(wait, TimeUnit.SECONDS);
    }

    public void setScriptWaitInSeconds(int wait) {
        driver.manage().timeouts().setScriptTimeout(wait, TimeUnit.SECONDS);
    }
}
