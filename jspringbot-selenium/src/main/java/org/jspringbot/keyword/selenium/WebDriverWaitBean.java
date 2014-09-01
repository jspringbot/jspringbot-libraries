package org.jspringbot.keyword.selenium;

import org.openqa.selenium.WebDriver;

import java.util.concurrent.TimeUnit;

public class WebDriverWaitBean {

    private WebDriver driver;
    
    private int downloadTimeoutInSeconds;

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
    
	public int getDownloadTimeoutInSeconds() {
		return downloadTimeoutInSeconds;
	}

	public void setDownloadTimeoutInSeconds(int downloadTimeoutInSeconds) {
		this.downloadTimeoutInSeconds = downloadTimeoutInSeconds;
	}
    
}
