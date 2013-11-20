package org.jspringbot.keyword.selenium;

import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class DesiredCapabilitiesBean {
    private DesiredCapabilities capabilities;

    private Proxy proxy;

    public DesiredCapabilitiesBean(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public void setFirefoxProfile(FirefoxProfile profile) {
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
    }

    public void setProxy(String proxyHost) {
        if(!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            proxy = new Proxy();

            proxy.setFtpProxy(proxyHost)
                 .setHttpProxy(proxyHost)
                 .setSslProxy(proxyHost);

            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
    }

    public void setSslProxy(String proxyHost) {
        if(!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            proxy = new Proxy();
            proxy.setSslProxy(proxyHost);

            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
    }

    public void setFtpProxy(String proxyHost) {
        if(!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            proxy = new Proxy();
            proxy.setFtpProxy(proxyHost);

            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
    }

    public void setHttpProxy(String proxyHost) {
        if(!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
            proxy = new Proxy();
            proxy.setHttpProxy(proxyHost);

            capabilities.setCapability(CapabilityType.PROXY, proxy);
        }
    }

}
