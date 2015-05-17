package org.jspringbot.keyword.selenium;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.Platform;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DesiredCapabilitiesBean implements InitializingBean {
    private DesiredCapabilities capabilities;

    private Proxy proxy;

    private File baseDir;

    private Map<String, Object> chromeOptions = new HashMap<String, Object>();

    private Map<String, String> mobileEmulation;

    private LoggingPreferences logPrefs;

    public DesiredCapabilitiesBean(DesiredCapabilities capabilities) {
        this.capabilities = capabilities;
    }

    public void setFirefoxProfile(FirefoxProfile profile) {
        capabilities.setCapability(FirefoxDriver.PROFILE, profile);
    }

    public void setBaseDir(File baseDir) {
        this.baseDir = baseDir;

        if(!baseDir.isDirectory()) {
            baseDir.mkdirs();
        }
    }

    public void setChromeDrivers(Map<OsCheck.OSType, Resource> chromeDrivers) throws IOException {
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();

        Resource chromeDriver = chromeDrivers.get(OsCheck.OSType.MacOS);

        if(chromeDriver == null) {
            throw new IllegalArgumentException("Unsupported OS " + osType.name());
        }

        File driverDir;
        if(baseDir != null) {
            driverDir = baseDir;
        } else {
            String userHome = System.getProperty("user.home");
            driverDir = new File(userHome);
        }

        File driver = unzip(chromeDriver.getInputStream(), driverDir);
        driver.setExecutable(true);

        System.setProperty("webdriver.chrome.driver", driver.getAbsolutePath());
    }

    public void setChromeOptions(Map<String, Object> chromeOptions) {
        this.chromeOptions = chromeOptions;
    }

    public void setChromeDeviceEmulation(String deviceEmulation) {
        if(StringUtils.isNotBlank(deviceEmulation) && !StringUtils.equalsIgnoreCase(deviceEmulation, "none")) {
            this.mobileEmulation = new HashMap<String, String>();
            mobileEmulation.put("deviceName", deviceEmulation);
        }
    }

    public void setChromeBrowserLog(String level) {
        if(StringUtils.isNotBlank(level) && !StringUtils.equalsIgnoreCase(level, "none")) {
            if(logPrefs == null) {
                logPrefs = new LoggingPreferences();
            }

            logPrefs.enable(LogType.BROWSER, Level.parse(level));
        }
    }

    public void setChromePerformanceLog(String level) {
        if(StringUtils.isNotBlank(level) && !StringUtils.equalsIgnoreCase(level, "none")) {
            if(logPrefs == null) {
                logPrefs = new LoggingPreferences();
            }

            logPrefs.enable(LogType.PERFORMANCE, Level.parse(level));
        }
    }

    public static File unzip(InputStream in, File dir) throws IOException {
        ZipInputStream zin = null;
        byte[] buf = new byte[2048];

        File entryFile = null;
        try {
            zin = new ZipInputStream(in);

            ZipEntry entry;
            while((entry = zin.getNextEntry()) != null) {
                FileOutputStream out = null;
                entryFile = new File(dir, entry.getName());

                try {
                    out = new FileOutputStream(entryFile);
                    int len;
                    while((len = zin.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                } finally {
                    IOUtils.closeQuietly(out);
                }
            }
        } finally {
            IOUtils.closeQuietly(in);
            IOUtils.closeQuietly(zin);
        }

        return entryFile;
    }

    public void setChromeLogFile(String logFile) {
        if(StringUtils.isNotBlank(logFile) && !StringUtils.equalsIgnoreCase(logFile, "none")) {
            File file = new File(logFile);
            File dir = file.getParentFile();

            if(dir != null && !dir.isDirectory()) {
                dir.mkdirs();
            }

            System.setProperty("webdriver.chrome.logfile", file.getAbsolutePath());
        }
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

    public void afterPropertiesSet() throws Exception {
        if(MapUtils.isNotEmpty(mobileEmulation)) {
            chromeOptions.put("mobileEmulation", mobileEmulation);
        }
        if(MapUtils.isNotEmpty(chromeOptions)) {
            capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);
        }
        if(logPrefs != null) {
            capabilities.setCapability(CapabilityType.LOGGING_PREFS, logPrefs);
        }
    }
}
