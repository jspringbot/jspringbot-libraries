package org.jspringbot.keyword.selenium;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.Augmenter;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;

public class RemoteWebDriverFactory {

    public static WebDriver create(URL remoteHost, Capabilities capabilities) {
        RemoteWebDriver webDriver = new RemoteWebDriver(remoteHost, capabilities);

        return new Augmenter().augment(webDriver);
    }
}
