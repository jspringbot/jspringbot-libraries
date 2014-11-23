package org.jspringbot.keyword.selenium;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

@Ignore
public class SelbenSeleniumTest {

    private WebDriver driver;
    
    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capability = DesiredCapabilities.firefox();
        driver = new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub"), capability);
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    
    @Test
    public void foo() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        Object retval = null;

        try {
            //driver.get("http://localhost:8080/selben.html");
            driver.get("http://localhost:8080/flex/Main.html");
            Thread.sleep(3000);
            
            retval = js.executeScript("return window.document['selben']" +
                    ".getForSelenium('<VerifyProperty value=\"combo\" propertyString=\"selectedLabel\"/>', 'waitForFlexMonkey')");
            
            System.out.println(retval);
            assertEquals("apple", String.valueOf(retval));
            Thread.sleep(3000);
            
        } catch (WebDriverException e) {
            e.printStackTrace();
            fail(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            fail(e.getMessage());
        } finally {
            driver.close();
        }
    }
    

}
