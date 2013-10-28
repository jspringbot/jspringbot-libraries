package org.jspringbot.keyword.selenium.web;

import org.jspringbot.KeywordInfo;
import org.jspringbot.keyword.selenium.AbstractSeleniumKeyword;
import org.jspringbot.keyword.selenium.FirefoxProfileBean;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@KeywordInfo(
        name = "Download Should Exists",
        parameters = {"filename"},
        description = "classpath:desc/DownloadShouldExists.txt"
)
public class DownloadShouldExists extends AbstractSeleniumKeyword implements ApplicationContextAware {

    private FirefoxProfileBean profileBean;

    private WebDriver driver;

    @Override
    public Object execute(Object[] params) throws Exception {
        if(profileBean == null) {
            throw new IllegalStateException("Keyword not supported, FirefoxProfileBean not defined.");
        }

        String filename = String.valueOf(params[0]);
        final File downloadDir = profileBean.getDownloadDir();
        final File download = new File(downloadDir, filename);

        (new WebDriverWait(driver, 10)).until(new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver d) {
                return download.isFile();
            }
        });

        if(!download.isFile()) {
            throw new IllegalArgumentException(String.format("Download file '%s' not found.", filename));
        }

        return null;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        try {
            profileBean = applicationContext.getBean(FirefoxProfileBean.class);
            driver = applicationContext.getBean(WebDriver.class);
        } catch(BeansException ignore) {
        }
    }
}
