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
import org.openqa.selenium.firefox.FirefoxProfile;

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

    public void setDownloadShowWhenStarting(boolean showWhenStarting) {
        profile.setPreference("browser.download.manager.showWhenStarting", showWhenStarting);
    }

    public void setDownloadSaveToDisk(String mimeType) {
        profile.setPreference("browser.helperApps.neverAsk.saveToDisk", mimeType);
    }

    public void setExtension(File extensionToInstall) throws IOException {
        profile.addExtension(extensionToInstall);
    }

    public void setExtensions(List<File> extensionToInstalls) throws IOException {
        for(File extensionToInstall : extensionToInstalls) {
            profile.addExtension(extensionToInstall);
        }
    }
}
