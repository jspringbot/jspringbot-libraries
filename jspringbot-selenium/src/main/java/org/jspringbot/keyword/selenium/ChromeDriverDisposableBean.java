package org.jspringbot.keyword.selenium;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.logging.LogEntry;
import org.springframework.beans.factory.DisposableBean;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;
import java.util.zip.GZIPOutputStream;

public class ChromeDriverDisposableBean implements DisposableBean {

    private ChromeDriver driver;

    private File chromePrefLogDir;

    public ChromeDriverDisposableBean(ChromeDriver driver) {
        this.driver = driver;
    }

    public void setChromePrefLogDir(String chromeLogDir) {
        if(StringUtils.isNotBlank(chromeLogDir) && !StringUtils.equalsIgnoreCase(chromeLogDir, "none")) {
            chromePrefLogDir = new File(chromeLogDir);

            if(!chromePrefLogDir.isDirectory()) {
                chromePrefLogDir.mkdirs();
            }
        }
    }

    public void destroy() throws Exception {
        if(chromePrefLogDir != null && chromePrefLogDir.isDirectory()) {
            Set<String> available = driver.manage().logs().getAvailableLogTypes();
            for (String type : available) {
                printLog(type);
            }
        }

        driver.close();
        driver.quit();
    }

    void printLog(String type) throws IOException {
        File logFile = new File(chromePrefLogDir, type + ".json.log.gz");
        PrintWriter out = null;

        try {
            for (LogEntry entry : driver.manage().logs().get(type)) {
                if (out == null) {
                    out = new PrintWriter(new GZIPOutputStream(new FileOutputStream(logFile)));
                }

                out.println(entry.getMessage());
            }
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}
