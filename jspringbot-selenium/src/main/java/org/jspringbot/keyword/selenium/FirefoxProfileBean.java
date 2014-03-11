/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.selenium;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class FirefoxProfileBean {
    protected FirefoxProfile profile;

    protected File downloadDir;

    public FirefoxProfileBean(FirefoxProfile profile) {
        this.profile = profile;
    }

    public void setDownloadDirectory(String downloadDirectory) throws IOException {
        downloadDir = new File(downloadDirectory);
        if(downloadDir.isDirectory()) {
            FileUtils.deleteDirectory(downloadDir);
        }
        
        if(!downloadDir.isDirectory()) {
            downloadDir.mkdirs();
        }

        profile.setPreference("browser.download.folderList", 2);
        profile.setPreference("browser.download.dir", downloadDirectory);
    }

    public File getDownloadDir() {
        return downloadDir;
    }

    public void setDownloadShowWhenStarting(boolean showWhenStarting) {
        profile.setPreference("browser.download.manager.showWhenStarting", showWhenStarting);
    }

    public void setDownloadSaveToDisk(String mimeType) {
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeType);
    }
    
    public void setNetworkProxyHTTP(String proxyHost) {
        if(!StringUtils.equalsIgnoreCase(proxyHost, "none")) {
        	profile.setPreference("network.proxy.http", proxyHost);
        }
    }
    
    public void setNetworkProxyHTTPPort(int proxyPort) {
    	profile.setPreference("network.proxy.http_port", proxyPort);
    }
    
    public void setNetworkProxyType(int proxyType) {
    	profile.setPreference("network.proxy.type", proxyType);
    }
    
    public void setNetworkProxyNoProxiesOn(String noProxiesOn) {
    	if(!StringUtils.equalsIgnoreCase(noProxiesOn, "none")) {
    		profile.setPreference("network.proxy.no_proxies_on", noProxiesOn);
    	}
    }
    
    public void setNetworkProxyShareProxySettings(boolean shareProxySettings) {
    	profile.setPreference("network.proxy.share_proxy_settings", shareProxySettings);
    }

    public void setNetworkHTTPPhishyUserpassLength(int length) {
    	profile.setPreference("network.http.phishy-userpass-length", length);
    }
    
    public void setNetworkAutomaticNtlmAuthTrustedURIs(String domain) {
    	profile.setPreference("network.automatic-ntlm-auth.trusted-uris", domain);
    }

    public void setNetworkAutomaticNtlmAuthAllowNonFqdn(boolean allow) {
    	profile.setPreference("network.automatic-ntlm-auth.allow-non-fqdn", allow);
    }
    
    public void setNetworkNtlmSendLmResponse(boolean sendResponse) {
    	profile.setPreference("network.ntlm.send-lm-response", sendResponse);
    }    

    public void setExtension(File extensionToInstall) throws IOException {
        profile.addExtension(extensionToInstall);
    }

    public void setExtensions(List<File> extensionToInstalls) throws IOException {
        for(File extensionToInstall : extensionToInstalls) {
            profile.addExtension(extensionToInstall);
        }
    }

    public void setUserAgent(String userAgent) {
        if (!StringUtils.equalsIgnoreCase(userAgent,"none")) {
            profile.setPreference("general.useragent.override", userAgent);
        }
        
    }

}
