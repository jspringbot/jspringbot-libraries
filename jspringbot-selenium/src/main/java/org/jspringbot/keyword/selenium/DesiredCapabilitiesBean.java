package org.jspringbot.keyword.selenium;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.Iterator;

public class DesiredCapabilitiesBean {
    private DesiredCapabilities capabilities;

    private Proxy proxy;

    public DesiredCapabilitiesBean(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public void setFirefoxProfile(FirefoxProfile profile) {
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
    }

    public void setBrowserName(String browserName) {
        if(!StringUtils.equalsIgnoreCase(browserName, "none")) {
            capabilities.setBrowserName(browserName);
        }
    }

    public void setVersion(String version){
        if(!StringUtils.equalsIgnoreCase(version, "none")) {
            capabilities.setVersion(version);
        }
    }

    public void setPlatform(String platform){
        if(!StringUtils.equalsIgnoreCase(platform, "none")) {
            capabilities.setPlatform(Platform.valueOf(platform));
        }
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

    @SuppressWarnings("unchecked")
    public void setCapabilities(String properties) throws JSONException {
        if(!StringUtils.equalsIgnoreCase(properties, "none")) {
            JSONObject obj = new JSONObject(properties);

            Iterator<String> itr = obj.keys();
            while(itr.hasNext()) {
                String key = itr.next();
                capabilities.setCapability(key, obj.getString(key));
            }
        }
    }

}
