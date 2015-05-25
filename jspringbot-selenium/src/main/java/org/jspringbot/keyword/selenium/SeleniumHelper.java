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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jsoup.helper.Validate;
import org.jspringbot.keyword.selenium.action.CustomActions;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.jspringbot.syntax.KeywordAppender;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumHelper {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(SeleniumHelper.class);

    public static long DEFAULT_POLL_MILLIS = 200l;

    protected WebDriver driver;

    protected ElementFinder finder;

    protected JavascriptExecutor executor;

    protected boolean cancelOnNextConfirmation = false;

    protected int screenCaptureCtr = 0;

    protected long screenCaptureSeed = System.currentTimeMillis();

    protected File screenCaptureDir;

    private Set<String> zoomedDomain = new HashSet<String>();

    private int autoZoomOut = 0;

    private int autoZoomIn = 0;

    private int implicitWaitInSeconds;

    private String jqueryLink;

    private CustomActions actions;

    public SeleniumHelper() {}

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        this.finder = new ElementFinder(driver);
    }

    @Required
    public void setImplicitWaitInSeconds(int implicitWaitInSeconds) {
        this.implicitWaitInSeconds = implicitWaitInSeconds;
    }

    public void setJqueryLink(String jqueryLink) {
        this.jqueryLink = jqueryLink;
    }

    public void setAutoZoomOut(int autoZoomOut) {
        this.autoZoomOut = autoZoomOut;
    }

    public void setAutoZoomIn(int autoZoomIn) {
        this.autoZoomIn = autoZoomIn;
    }

    public void windowMaximize() {
        driver.manage().window().maximize();
    }

    public void setSize(int width, int height) {
        LOG.keywordAppender()
                .appendArgument("width", width)
                .appendArgument("height", height);

        driver.manage().window().setSize(new Dimension(width, height));
    }

    public WebDriver getDriver() {
        return this.driver;
    }
    
    public void assignIdToElement(String id, String locator) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("id", id);

        WebElement el = finder.find(locator);

        executor.executeScript(String.format("arguments[0].id = '%s';", id), el);
    }

    public Boolean findElement(String locator){
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator)) {
            LOG.info(String.format("Page should have contained element '%s' but did not.", locator));
            return false;
        }

        return true;
    }
    
    public void setScreenCaptureDir(File screenCaptureDir) {
        this.screenCaptureDir = screenCaptureDir;

        // create dir if not existing
        if(!screenCaptureDir.isDirectory()) {
            screenCaptureDir.mkdirs();
        }
    }

    public void navigateTo(String url) {
        driver.navigate().to(url);

        String domain = getDomain(url);

        KeywordAppender appender = LOG.keywordAppender();
        appender.appendArgument("link", url);

        if(autoZoomIn != 0 || autoZoomOut != 0) {
            appender.appendArgument("domain", domain);
            appender.appendArgument("autoZoomIn", autoZoomIn);
            appender.appendArgument("autoZoomOut", autoZoomOut);
        }

        if(domain != null && !hasZoomed(domain)) {
            if(autoZoomIn > 0) {
                zoomIn(autoZoomIn);
            }

            if(autoZoomOut > 0) {
                zoomOut(autoZoomOut);
            }

            zoomedDomain.add(domain);
        }
    }

    private boolean hasZoomed(String domain) {
        if(zoomedDomain.contains(domain)) {
            return true;
        }

        for(String item : zoomedDomain) {
            if(StringUtils.contains(domain, item) || StringUtils.contains(item, domain)) {
                return true;
            }
        }

        return false;
    }

    private String getDomain(String urlString) {
        try {
            URL url = new URL(urlString);

            return StringUtils.lowerCase(url.getHost());
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeBrowser() {
        LOG.info("Closing browser");
        driver.close();
    }

    public void quitBrowser() {
        LOG.info("Quiting browser");
        driver.quit();
    }

    public void alertShouldBePresent(String expectedAlertText) {
        String actualAlertText = getAlertMessage();

        LOG.keywordAppender()
                .appendArgument("Actual Alert Text", actualAlertText)
                .appendArgument("Expected Alert Text", expectedAlertText);

        if (!StringUtils.equals(actualAlertText, expectedAlertText)) {
            throw new IllegalArgumentException("Alert text is not equal.");
        }
    }

    public String getAlertMessage() {
        return closeAlert();
    }

    public String confirmAction() {
        String text = null;

        try{
            Alert alert = driver.switchTo().alert();
            text = alert.getText();

            if (cancelOnNextConfirmation) {
               alert.dismiss();
            } else {
                alert.accept();
            }
        } catch (NoAlertPresentException e) {
            LOG.info("No alert is present. That's fine..");
        }

        cancelOnNextConfirmation = false;

        return text;
    }

    public void chooseCancelOnNextConfirmation() {
        cancelOnNextConfirmation = true;
    }

    public void checkboxShouldBeSelected(String locator){
        WebElement el = getCheckbox(locator);

        LOG.keywordAppender()
                .appendBold("Checkbox Should Be Selected:")
                .appendLocator(locator)
                .appendArgument("Selected", el.isSelected());

        if (!el.isSelected()) {
            throw new IllegalArgumentException("Checkbox should have been selected.");
        }
    }

    public void checkboxShouldNotBeSelected(String locator) {
        WebElement el = getCheckbox(locator);

        LOG.keywordAppender()
                .appendBold("Checkbox Should Not e Selected:")
                .appendLocator(locator)
                .appendArgument("Selected", el.isSelected());

        if (el.isSelected()) {
            throw new IllegalArgumentException("Checkbox should not have been selected.");
        }
    }

    public void chooseFile(String locator, String filePath) {
        File file = new File(filePath);

        LOG.keywordAppender().appendLocator(locator)
                .appendArgument("File Path", filePath)
                .appendArgument("Is File", file.isFile());

        if (!file.isFile()) {
            throw new IllegalArgumentException("File does not exist on the local file system.");
        }

        WebElement el = finder.find(locator);
        el.sendKeys(toKeys(filePath));
    }
    
    public void clickAt(String locator, String xOffset, String yOffset) {
        LOG.keywordAppender().appendLocator(locator);

        int offSetX = Integer.parseInt(xOffset);
        int offSetY = Integer.parseInt(yOffset);

        LOG.keywordAppender()
                .appendArgument("offSetX", offSetX)
                .appendArgument("offSetY", offSetY);

        WebElement el = finder.find(locator);

        if(el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).moveToElement(el).moveByOffset(offSetX, offSetY).click().perform();
    }

    public void clickButton(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement button = finder.find(locator, false, "input");
        if (button == null) {
            button = finder.find(locator, "button");
        }

        button.click();
    }

    public void clickElement(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        finder.find(locator).click();
    }

    public void clickImage(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement image = finder.find(locator, false, "img");

        if (image == null) {
            image = finder.find(locator,"input");
        }

        image.click();
    }

    public void clickLink(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);
        el.click();
    }

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<String>();

        WebElement el = finder.find("tag=a",false,"a");
        links.add(el.getAttribute("id"));

        return links;
    }

    public File elementCaptureScreenShot(String locator) throws IOException {
        return elementCaptureScreenShot(locator, null);
    }

    public File elementCaptureScreenShot(String locator, String options) throws IOException {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        File file = newScreenCaptureFile();

        LOG.keywordAppender().appendArgument("File", file.getAbsolutePath());

        LOG.html("Screen captured (%d): <br /> <img src='%s'/>", screenCaptureCtr, file.getName());

        BufferedImage fullImg = ImageIO.read(new ByteArrayInputStream(bytes));
        //Get the location of element on the page
        Point point = el.getLocation();
        //Get width and height of the element
        int eleWidth = el.getSize().getWidth();
        int eleHeight = el.getSize().getHeight();

        LOG.keywordAppender()
                .appendArgument("Width", eleWidth)
                .appendArgument("Height", eleHeight)
                .appendArgument("X", point.getX())
                .appendArgument("Y", point.getY());

        //Crop the entire page screenshot to get only element screenshot
        BufferedImage eleScreenshot= fullImg.getSubimage(point.getX(), point.getY(), eleWidth, eleHeight);

        if(options != null) {
            eleScreenshot = processOption(eleScreenshot, options);
        }

        ImageIO.write(eleScreenshot, "png", file);

        return file;
    }

    private Map<String, String> getOptions(String options) {
        try {
            String[] items = StringUtils.split(options, ",");

            Map<String, String> map = new HashMap<String, String>(items.length);
            for (String item : items) {
                String[] result = StringUtils.split(item, "=");

                if (result.length == 2) {
                    map.put(result[0], result[1]);
                }
            }

            return map;
        } catch(Exception e) {
            throw new IllegalArgumentException("Invalid option: " + options);
        }
    }

    public File captureScreenShot() throws IOException {
        return captureScreenShot(null);
    }

    public File captureScreenShot(String options) throws IOException {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        File file = newScreenCaptureFile();

        if(options == null) {
            FileOutputStream out = null;
            try {

                LOG.html("Screen captured (%d): <br /> <img src='%s'/>", screenCaptureCtr, file.getName());

                out = new FileOutputStream(file);
                IOUtils.write(bytes, out);

                return file;
            } finally {
                IOUtils.closeQuietly(out);
            }
        } else {
            LOG.html("Screen captured (%d): <br /> <img src='%s'/>", screenCaptureCtr, file.getName());

            BufferedImage fullImg = ImageIO.read(new ByteArrayInputStream(bytes));
            ImageIO.write(processOption(fullImg, options), "png", file);
        }

        return file;
    }

    private BufferedImage processOption(BufferedImage fullImg, String options) {
        Map<String, String> optionMap = getOptions(options);

        if(optionMap.containsKey("x") && optionMap.containsKey("y") && optionMap.containsKey("width") && optionMap.containsKey("height")) {
            int x = Integer.parseInt(optionMap.get("x"));
            int y = Integer.parseInt(optionMap.get("y"));
            int width = Integer.parseInt(optionMap.get("width"));
            int height = Integer.parseInt(optionMap.get("height"));

            LOG.keywordAppender()
                    .appendArgument("option.width", width)
                    .appendArgument("option.height", height)
                    .appendArgument("option.x", x)
                    .appendArgument("option.y", y);

            fullImg = fullImg.getSubimage(x, y, width, height);
        }

        String style = optionMap.get("style");
        if(StringUtils.equals(style, "grayscale")) {
            LOG.keywordAppender().appendArgument("option.grayscaled", true);

            toGray(fullImg);
        }

        return fullImg;
    }

    private void toGray(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        for(int i=0; i<height; i++){
            for(int j=0; j<width; j++){
                Color c = new Color(image.getRGB(j, i));
                int red = (int)(c.getRed() * 0.21);
                int green = (int)(c.getGreen() * 0.72);
                int blue = (int)(c.getBlue() *0.07);
                int sum = red + green + blue;
                Color newColor = new Color(sum,sum,sum);
                image.setRGB(j,i,newColor.getRGB());
            }
        }
    }

    public void addCookie(String cookieName, String cookieValue, String host, String domain, String path) {
        LOG.keywordAppender()
                .appendArgument("Cookie Name", cookieName)
                .appendArgument("Cookie Value", cookieValue)
                .appendArgument("Host", host)
                .appendArgument("Domain", domain)
                .appendArgument("Path", path);

        driver.get(host);

        Cookie cookie = new Cookie(cookieName, cookieValue, domain, path, null);
        driver.manage().addCookie(cookie);
    }

    public void deleteAllCookies() {
        Set<Cookie> allCookies = getCookies();
        for (Cookie loadedCookie : allCookies) {
            deleteCookie(loadedCookie);
        }
    }

    public void deleteCookie(String cookieName) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);

        deleteCookie(cookie);
    }

    public String getCookieValue (String cookieName) {
        LOG.keywordAppender().appendArgument("Cookie Name", cookieName);

        Cookie cookie = driver.manage().getCookieNamed(cookieName);

        if (cookie != null) {
            LOG.keywordAppender().appendArgument(cookie.getName(), cookie.getValue());

            return cookie.getValue();
        } else {
            throw new IllegalStateException(String.format("Cookie with name '%s' not found", cookieName));
        }
    }

    public Set<Cookie> getCookies () {
        Set<Cookie> cookies = driver.manage().getCookies();

        for(Cookie cookie : cookies) {
            LOG.keywordAppender().appendArgument(cookie.getName(), cookie.getValue());
        }

        return cookies;
    }

    public void doubleClickElement(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        new Actions(driver).doubleClick(el).perform();
    }

    public void dragAndDrop(String locatorSrc, String locatorDest) {
        LOG.keywordAppender()
                .appendLocator("Source: " + locatorSrc)
                .appendLocator("Destination: " + locatorDest);

        WebElement element = finder.find(locatorSrc);
        WebElement target = finder.find(locatorDest);

        new Actions(driver).dragAndDrop(element, target).perform();
    }

    public void dragAndDropByOffset(String locatorSrc, int xOffset, int yOffset) {
        LOG.keywordAppender()
                .appendLocator(locatorSrc)
                .appendArgument("xOffset", xOffset)
                .appendArgument("yOffset", yOffset);

        WebElement element = finder.find(locatorSrc);

        new Actions(driver).dragAndDropBy(element, xOffset, yOffset).perform();
    }

    public void elementShouldBeDisabled(String locator) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Enabled", isEnabled(locator));

        if (isEnabled(locator))  {
            throw new AssertionError("Element is enabled.");
        }
    }

    public void elementShouldBeEnabled(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        boolean isEnabled = isEnabled(locator);


        LOG.keywordAppender().appendArgument("Enabled", isEnabled);

        if (!isEnabled)  {
            throw new AssertionError("Element is disabled");
        }
    }

    public void elementShouldBeVisible(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        boolean isVisible = isVisible(locator);

        LOG.keywordAppender().appendArgument("Visible", isVisible);

        if (!isVisible) {
            throw new AssertionError("The element should be visible, but it is not.");
        }
    }
    
    public boolean isElementVisible(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        boolean isVisible = isVisible(locator);

        LOG.keywordAppender().appendArgument("Visible", isVisible);

        return isVisible;
    }

    public void elementShouldNotBeVisible(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        boolean isVisible = isVisible(locator);

        LOG.keywordAppender().appendArgument("Visible", isVisible);

        if (isVisible) {
            throw new AssertionError("The element should not be visible, but it is not.");
        }
    }

    public void currentFrameShouldContain(String text) {
        LOG.keywordAppender().appendArgument("text", text);
        boolean textIsPresent = textIsPresent(text);

        LOG.keywordAppender().appendArgument("Text Is Present", textIsPresent);

        if (!textIsPresent) {
            throw new AssertionError("Page should have contained text but did not.");
        }
    }

    public void fireEvent(String locator, String eventName) throws IOException {
        fireEvent(locator, "HTMLEvent", eventName);
    }

    public void fireEvent(String locator, String event, String eventName) throws IOException {
        executeJavascript(locator, "classpath:js/fireEvent.js", event, eventName);
    }

    public void frameShouldContainText(String locator, String text) {
        LOG.keywordAppender().appendArgument("text", text);
        boolean frameContains = frameContains(locator, text);

        LOG.keywordAppender().appendArgument("Text Is Present", frameContains);

        if (!frameContains) {
            throw new AssertionError("Page should have contained text but did not.");
        }
    }
    
    public void elementShouldContain(String locator, String expected) {
        LOG.keywordAppender().appendLocator(locator);
        String actual = getText(locator, false);

        LOG.keywordAppender()
                .appendArgument("Actual", actual)
                .appendArgument("Expected", expected);
       
        if(!StringUtils.contains(StringUtils.trim(actual), StringUtils.trim(expected))) {
            throw new AssertionError("Element should have contained text.");
        }
    }

    public void elementShouldContainClass(String locator, String expectedClassName) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, true);

        String classNames = el.getAttribute("class");

        LOG.keywordAppender()
                .appendArgument("Class Names", classNames)
                .appendArgument("Expected Class Name", expectedClassName);

        if(StringUtils.isNotEmpty(classNames)) {
            if(Arrays.asList(StringUtils.split(classNames, " ")).contains(expectedClassName)) {
                return;
            }
        }

        throw new AssertionError("Element should have contained class.");
    }

    public void elementShouldContainType(String locator, String expectedTypeName) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, true);

        String typeNames = el.getAttribute("type");

        LOG.keywordAppender()
                .appendArgument("Class Names", typeNames)
                .appendArgument("Expected Type Name", expectedTypeName);

        if(StringUtils.isNotEmpty(typeNames)) {
            if(Arrays.asList(StringUtils.split(typeNames, " ")).contains(expectedTypeName)) {
                return;
            }
        }

        throw new AssertionError("Element should have contained type.");
    }

    public void elementShouldNotContainClass(String locator, String expectedClassName) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, true);

        String classNames = el.getAttribute("class");

        LOG.keywordAppender()
                .appendArgument("Class Names", classNames)
                .appendArgument("Expected Class Name", expectedClassName);

        if(StringUtils.isNotEmpty(classNames)) {
            if(Arrays.asList(StringUtils.split(classNames, " ")).contains(expectedClassName)) {
                throw new AssertionError("Element should have not contained class");
            }
        }
    }

    public void elementTextShouldBe(String locator, String expectedText) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        String actualText = el.getText();

        LOG.keywordAppender()
                .appendArgument("Actual Text", actualText)
                .appendArgument("Expected Text", expectedText);

        if (!StringUtils.equals(StringUtils.trim(expectedText), StringUtils.trim(actualText))) {
            throw new AssertionError("The text of element is not as expected.");
        }
    }

    public void simulateDragAndDrop(String cssSrcLocator, String cssDestLocator) throws IOException {
        if(StringUtils.startsWith(cssSrcLocator, "css=")) {
            cssSrcLocator = cssSrcLocator.substring(4);
        }
        if(StringUtils.startsWith(cssDestLocator, "css=")) {
            cssDestLocator = cssDestLocator.substring(4);
        }

        LOG.keywordAppender()
                .appendLocator("Source: css=" + cssSrcLocator)
                .appendLocator("Destination: css=" + cssDestLocator);

        if(!(Boolean) executeJavascript("return (typeof $ != 'undefined');")) {
            LOG.keywordAppender().appendArgument("jqueryLink", jqueryLink);
            executeJavascript(jqueryLink);
        }
        if(!(Boolean) executeJavascript("return (typeof $.fn.simulateDragDrop != 'undefined');")) {
            LOG.keywordAppender().appendArgument("dragAndDrop", "classpath:js/dragAndDrop.js");

            executeJavascript("classpath:js/dragAndDrop.js");
        }

        executeJavascript(String.format("$('%s').simulateDragDrop({ dropTarget: $('%s')});", cssSrcLocator, cssDestLocator));
    }

    public Object executeJavascript(String code) throws IOException {
        return executeJavascript(null, code);
    }

    public Object executeJavascript(String code, Object... args) throws IOException {
        return executeJavascript(null, code, args);
    }
     
    public Object executeJavascript(String locator, String code, Object... args) throws IOException {
        if(StringUtils.startsWith(code, "file:") || StringUtils.startsWith(code, "classpath:") || StringUtils.startsWith(code, "http:") || StringUtils.startsWith(code, "https:")) {
            LOG.keywordAppender().appendArgument("Resource", code);
            ResourceEditor editor = new ResourceEditor();
            editor.setAsText(code);
            Resource resource = (Resource) editor.getValue();

            code = new String(IOUtils.toCharArray(resource.getInputStream()));
        }

        if(locator != null) {
            LOG.keywordAppender().appendLocator(locator);
        }

        LOG.keywordAppender().appendJavascript(code);

        Object returnedValue;
        if(locator == null) {
            if(args == null || args.length == 0) {
                returnedValue = executor.executeScript(code);
            } else {
                returnedValue = executor.executeScript(code, args);
            }
        } else {
            WebElement el = finder.find(locator);

            if(args == null || args.length == 0) {
                returnedValue = executor.executeScript(code, el);
            } else {
                Object[] args1 = new Object[args.length + 1];
                System.arraycopy(args, 0, args1, 1, args.length + 1 - 1);
                args1[0] = el;
                returnedValue = executor.executeScript(code, args1);
            }
        }

        LOG.keywordAppender().appendArgument("Returned Value", returnedValue);

        return returnedValue;
    }

    public String getElementAttribute(String attributeLocator) {
        String[] parts = parseAttributeLocator(attributeLocator);
        String locator = parts[0];
        String attributeName = parts[1];

        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Attribute Name", attributeName);

        WebElement el = finder.find(parts[0], true);

        if (el == null) {
            throw new IllegalArgumentException("Element not found");
        }

        String attributeValue = el.getAttribute(attributeName);

        LOG.keywordAppender()
                .appendArgument("Attribute Value", attributeValue);

        return attributeValue;
    }
    
    public void elementAttributeValueShouldBe(String attributeLocator, String expectedValue) {
        LOG.keywordAppender().appendLocator(attributeLocator);

        String actualValue = getElementAttribute(attributeLocator);

        LOG.keywordAppender()
                .appendArgument("Actual Element Attribute Value", actualValue)
                .appendArgument("Expected Element Attribute Value", expectedValue);

        if (!StringUtils.equals(StringUtils.trim(actualValue), StringUtils.trim(expectedValue))) {
            throw new AssertionError("The attribute value of the element is not as expected.");
        }
    }
    
    public void elementAttributeValueShouldContain(String attributeLocator, String expectedValue){
        LOG.keywordAppender().appendLocator(attributeLocator);

        String actualValue = getElementAttribute(attributeLocator);

        LOG.keywordAppender()
                .appendArgument("Actual Element Attribute Value", actualValue)
                .appendArgument("Expected Element Attribute Value", expectedValue);

        if(!StringUtils.contains(StringUtils.trim(actualValue), StringUtils.trim(expectedValue))) {
            throw new AssertionError("Element attribute value should have contained text.");
        }
    }

    public int getHorizontalPosition(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement element = finder.find(locator);

        Point point = element.getLocation();

        LOG.keywordAppender()
                .appendArgument("X", point.getX())
                .appendArgument("Y", point.getY());

        return point.getX();
    }

    public int getVerticalPosition(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement element = finder.find(locator);

        Point point = element.getLocation();

        LOG.keywordAppender()
                .appendArgument("X", point.getX())
                .appendArgument("Y", point.getY());

        return point.getY();
    }

    public String getLocation() {
        LOG.keywordAppender()
                .appendArgument("Current URL", driver.getCurrentUrl());

        return driver.getCurrentUrl();
    }

    public int getMatchingCSSCount(String cssLocator) {
        if(StringUtils.startsWith(cssLocator, "css=")) {
            cssLocator = cssLocator.substring(4);
        }

        LOG.keywordAppender().appendLocator("css=" + cssLocator);

        int count = driver.findElements(By.cssSelector(cssLocator)).size();

        LOG.keywordAppender().appendArgument("Count", count);

        return count;
    }

    public int getMatchingXPathCount(String xpath) {
        if(StringUtils.startsWith(xpath, "xpath=")) {
            xpath = xpath.substring(6);
        }

        LOG.keywordAppender().appendLocator("xpath=" + xpath);

        int count = driver.findElements(By.xpath(xpath)).size();

        LOG.keywordAppender().appendArgument("Count", count);

        return count;
    }

    public String getSource() {
        String source = driver.getPageSource();

        LOG.keywordAppender().appendXML(source);

        return source;
    }


    public void selectedTextValueInListShouldBe(String locator, String selectedText) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        boolean textIsSelected = false;

        for (WebElement selected: selectOptions) {
            if (selected.isSelected() && selected.getText().equalsIgnoreCase(selectedText)) {
                textIsSelected = true;
                break;
            }
        }

        if (!textIsSelected) {
            throw new AssertionError("Value was not selected from list");
        }
    }

    public List<String> getListItems(String locator) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        List<String> labels = getLabelsForOptions(selectOptions);

        LOG.keywordAppender().appendArgument("Labels", labels);

        return labels;
    }

    public String getSelectedListLabel(String locator) {
        List<String> selectedLabels = getSelectedListLabels(locator);

        if (CollectionUtils.isEmpty(selectedLabels)) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have a single selected value", locator));
        }

        LOG.keywordAppender().appendArgument("Selected Label", selectedLabels.get(0));

        return selectedLabels.get(0);
    }

    public String getSelectedListValue(String locator) {
        List<String> selectedListValues = getSelectedListValues(locator);

        if (CollectionUtils.isEmpty(selectedListValues)) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have a single selected value",locator));
        }

        LOG.keywordAppender().appendArgument("Selected Value", selectedListValues.get(0));

        return selectedListValues.get(0);
    }


    public List<String> getSelectedListValues(String locator) {
        List<WebElement> selectedOptions = getSelectListOptionsSelected(locator);

        if (CollectionUtils.isEmpty(selectedOptions)) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have any selected values",locator));
        }

        List<String> values = getValuesForOptions(selectedOptions);

        LOG.keywordAppender().appendArgument("Selected Values", values);

        return values;
    }

    public List<String> getSelectLabels(String locator) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        if (selectOptions.size() == 0) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have any values",locator));
        }

        List<String> labels =  getLabelsForOptions(selectOptions);

        LOG.keywordAppender().appendArgument("Selected Labels", labels);

        return labels;
    }

    public List<String> getSelectedListLabels(String locator) {
        List<WebElement> selectedOptions = getSelectListOptionsSelected(locator);

        if (selectedOptions.size() == 0) {
            throw new IllegalArgumentException(String.format("Select list with locator %s does not have any selected values.", locator));
        }

        List<String> labels = getLabelsForOptions(selectedOptions);

        LOG.keywordAppender().appendArgument("Selected Labels", labels);

        return labels;
    }

    public void selectAllFromList(String locator) {
        WebElement selectEl = getSelectList(locator);
        boolean isMultiSelectList = isMultiSelectList(selectEl);

        if (!isMultiSelectList) {
            LOG.keywordAppender().appendArgument("multi-select", true);

            throw new IllegalArgumentException("Keyword 'Select all from list' works only for multi-select lists.");
        }

        List<WebElement> selectOptions = getSelectListOptions(selectEl);
        int index = 0;
        for(WebElement option : selectOptions) {
            if(!option.isSelected()) {
                LOG.keywordAppender().appendArgument(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText());
                option.click();
            } else {
                LOG.keywordAppender().appendArgument(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText() + ": (already selected)");
            }
        }
    }
    
    public void unselectAllFromList(String locator) {
        LOG.keywordAppender().appendLocator(locator);
        
        WebElement selectEl = getSelectList(locator);
        boolean isMultiSelectList = isMultiSelectList(selectEl);

        if (!isMultiSelectList) {
            LOG.keywordAppender().appendArgument("multi-select", false);
            throw new IllegalArgumentException("Keyword 'Unselect all from list' works only for multi-select lists.");
        }

        List<WebElement> selectOptions = getSelectListOptions(selectEl);
        int index = 0;
        for(WebElement option : selectOptions) {
            if(option.isSelected()) {
                LOG.keywordAppender().appendArgument(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText());
                option.click();
            } else {
                LOG.keywordAppender().appendArgument(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText() + ": (already unselected)");
            }
        }
    }
    

    public String getText(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        return getText(locator,true);
    }

    public String getTitle() {
        LOG.keywordAppender().appendArgument("title", driver.getTitle());

        return driver.getTitle();
    }

    public String getValue(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        return el.getAttribute("value");
    }
    
    public String getCSSValue(String locator, String propertyName) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Property Name", propertyName);

        WebElement el = finder.find(locator);

        return el.getCssValue(propertyName);
    }

    public void goBack() {
        driver.navigate().back();
    }
    
    public String getHTMLSourceOfOmnitureDebuggerWindow(final String javaScript, String windowName, String decodeCheckboxNameLocator, int waitTime) {
        String htmlSource = null;
        String parentWindowHandle = driver.getWindowHandle();
        executor.executeScript(javaScript);
        try {
            boolean windowFound = false;
            while (!windowFound) {
                {
                    driver = driver.switchTo().window(windowName);
                    WebElement element = driver.findElement(By.name(decodeCheckboxNameLocator));
                    if (!element.isSelected()) {
                        element.click();
                        while (!element.isSelected()) {
                            Thread.sleep(waitTime);
                        }
                    }
                    executor.executeScript("window.scrollBy(0,450)", ""); // scroll down to view the last image
                    Thread.sleep(waitTime);
                    htmlSource = driver.getPageSource();
                    driver.close();// child window closing
                    windowFound = true;
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        driver.switchTo().window(parentWindowHandle);
        // driver.close(); // do not close for another set of actions
        return htmlSource;
    }

    public void inputPassword(String locator, String password) {
        LOG.keywordAppender()
                .appendArgument("password", password)
                .appendLocator(locator);

        inputTextIntoTextField(locator, password);
    }

    public void inputText(String locator, String text) {
        LOG.keywordAppender()
                .appendArgument("text", text)
                .appendLocator(locator);

        inputTextIntoTextField(locator, text);
    }
    
    public void sendKeys(String locator, String text) {
        LOG.keywordAppender()
                .appendArgument("text", text)
                .appendLocator(locator);

        WebElement el = finder.find(locator);
        el.sendKeys(toKeys(text));
    }

    public void listSelectionShouldBe(String locator, List<String> items) {
        LOG.keywordAppender().appendArgument("Expected", items);
        List<String> values = getSelectedListValues(locator);

        LOG.keywordAppender().appendArgument("Actual Values", values);

        boolean containsValues = items.containsAll(values);

        if (containsValues) {
            return;
        }

        List<String> texts = getSelectedListLabels(locator);

        LOG.keywordAppender().appendArgument("Actual Texts", texts);

        boolean containsText = items.containsAll(texts);

        if (containsText) {
            return;
        }

        throw new IllegalArgumentException("Selection list not found");
    }

    public void listValueSelectionShouldBe(String locator, List<String> items) {
        LOG.keywordAppender().appendArgument("Expected", items);

        List<String> values = getSelectedListValues(locator);

        boolean containsValues = items.containsAll(values);

        if(containsValues) {
            LOG.keywordAppender().appendArgument("Actual Values", values);

            return;
        }

        throw new IllegalArgumentException("Selection value for list not found");
    }

    public void listTextSelectionShouldBe(String locator, List<String> items) {
        LOG.keywordAppender().appendArgument("Expected", items);

        List<String> texts = getSelectedListLabels(locator);
        boolean containsText = items.containsAll(texts);

        if(containsText) {
            LOG.keywordAppender().appendArgument("Actual", texts);

            return;
        }

        throw new IllegalArgumentException("Selection text list not found");
    }

    public void listShouldHaveNoSelection(String locator) {
        List<String> values = getListItems(locator);

        if(CollectionUtils.isNotEmpty(values)) {
            LOG.keywordAppender()
                    .appendArgument("Values", values)
                    .appendArgument("Texts", getSelectedListLabels(locator));

            throw new IllegalArgumentException("List should have no selection.");
        }
    }

    public void selectFromListByIndex(String locator, List<Integer> indices) {
        LOG.keywordAppender().appendArgument("Indices", indices);

        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            if(indices.contains(i)) {
                WebElement option = options.get(i);
                LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(indices.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }
    }

    public void unselectFromListByIndex(String locator, List<Integer> indices) {
        LOG.keywordAppender().appendArgument("Indices", indices);

        List<WebElement> options = getSelectListOptions(locator);
        
        for(int i : indices) {
            WebElement option = options.get(i);
            boolean isSelected = option.isSelected();

            //if not selected, dont unselect since it'll just click the option--thus selecting it.
            if(isSelected) {
                LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                option.click();
            }
            
        }
    }
    
    public void selectFromListByValue(String locator, List<String> values) {
        LOG.keywordAppender().appendArgument("Values", values);

        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);

            if(values.contains(option.getAttribute("value"))) {
                LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(values.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }
    }
    
    public void unselectFromListByValue(String locator, List<String> values) {
        LOG.keywordAppender().appendArgument("Values", values);

        List<WebElement> options = getSelectListOptions(locator);
        
        for(int i = 0; i < options.size(); i++) {            
            WebElement option = options.get(i);
            
            if(values.contains(option.getAttribute("value"))) {    
                boolean isSelected = option.isSelected();
                
                //if not selected, dont unselect since it'll just click the option--thus selecting it.
                if(isSelected) {
                    LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                    option.click();
                }
                
                if(values.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }
    }

    public void selectFromListByLabel(String locator, List<String> labels) {
        LOG.keywordAppender().appendArgument("Labels", labels);

        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);

            if(labels.contains(option.getText())) {
                LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(labels.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }
    }
    
    public void unselectFromListByLabel(String locator, List<String> labels) {
        LOG.keywordAppender().appendArgument("Labels", labels);

        List<WebElement> options = getSelectListOptions(locator);
        
        for(int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);
            
            if(labels.contains(option.getText())) {
                boolean isSelected = option.isSelected();
                
                //if not selected, dont unselect since it'll just click the option--thus selecting it.
                if(isSelected) {
                    LOG.keywordAppender().append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                    options.get(i).click();
                }
                
                if(labels.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }
    }

    public void selectFromList(String locator, List<String> items) {
        List<String> values = getSelectedListValues(locator);
        boolean containsValues = CollectionUtils.containsAny(values, items);

        if(containsValues) {
            selectFromListByValue(locator, items);

            return;
        }

        List<String> texts = getSelectedListLabels(locator);
        boolean containsText = CollectionUtils.containsAny(texts, items);

        if(containsText) {
            selectFromListByLabel(locator, items);
        }
    }
    
    public void unselectFromList(String locator, List<String> items) {
        List<String> values = getSelectedListValues(locator);
        boolean containsValues = CollectionUtils.containsAny(values, items);

        if(containsValues) {
            unselectFromListByValue(locator, items);

            return;
        }

        List<String> texts = getSelectedListLabels(locator);
        boolean containsText = CollectionUtils.containsAny(texts, items);

        if(containsText) {
            unselectFromListByLabel(locator, items);
        }
    }

    public void mouseDown(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).clickAndHold(el).release().perform();
    }

    public void mouseDownOnImage(String locator){
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, "image");

        new Actions(driver).clickAndHold(el).perform();
    }
    public void mouseDownOnLink(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, "link");

        new Actions(driver).clickAndHold(el).perform();
    }

    public void mouseOut(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        Dimension size = el.getSize();
        int offsetX = (size.getWidth() / 2 ) + 1;
        int offsetY = (size.getHeight() / 2 ) + 1;

        new Actions(driver).moveToElement(el).moveByOffset(offsetX, offsetY).perform();
    }

    public void actionStart() {
        actions = new CustomActions(this);
    }

    public void actionMoveToElement(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.moveToElement(el);
    }

    public void actionMoveToElement(String locator, int xOffset, int yOffset) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("xOffset", xOffset)
                .appendArgument("yOffset", yOffset);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.moveToElement(el, xOffset, yOffset);
    }


    public void actionMoveByOffset(int x, int y) {
        LOG.keywordAppender()
                .appendArgument("xOffset", x)
                .appendArgument("yOffset", y);

        Validate.notNull(actions, "actions is not yet started.");

        actions.moveByOffset(x, y);
    }

    public void actionClick(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.click(el);
    }

    public void actionWaitUntilElementFound(String locator) {
        Validate.notNull(actions, "actions is not yet started.");

        actions.waitUntilElementFound(locator);
    }

    public void actionWaitUntilElementVisible(String locator) {
        Validate.notNull(actions, "actions is not yet started.");

        actions.waitUntilElementVisible(locator);
    }

    public void actionDoubleClick() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.doubleClick();
    }

    public void actionDoubleClick(String locator) {
        Validate.notNull(actions, "actions is not yet started.");

        LOG.keywordAppender()
                .appendLocator(locator);

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.doubleClick(el);
    }

    public void actionClick() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.click();
    }

    public void actionClickAndHold(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.clickAndHold(el);
    }

    public void actionRelease() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.release();
    }

    public void actionRelease(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.release(el);
    }

    public void actionClickAndHold() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.clickAndHold();
    }

    public void actionContextClick() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.contextClick();
    }

    public void actionContextClick(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.contextClick(el);
    }

    public void actionDragAndDrop(String srclocator, String destLocation) {
        LOG.keywordAppender()
                .appendLocator("Source: " + srclocator)
                .appendLocator("Destination: " + destLocation);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement src = finder.find(srclocator);
        WebElement dest = finder.find(destLocation);

        if (src == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", srclocator));
        }
        if (dest == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", destLocation));
        }

        actions.dragAndDrop(src, dest);
    }

    public void actionSimulateDragAndDrop(String srcCsslocator, String destCssLocation) {
        Validate.notNull(actions, "actions is not yet started.");

        actions.simulateDragAndDrop(srcCsslocator, destCssLocation);
    }

    public void actionSendKeys(String keys) {
        LOG.keywordAppender().appendArgument("Keys", keys);

        Validate.notNull(actions, "actions is not yet started.");

        actions.sendKeys(toKeys(keys));
    }

    public void actionSendKeys(String locator, String keys) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Keys", keys);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement src = finder.find(locator);

        if (src == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.sendKeys(src, toKeys(keys));
    }

    private boolean isKeyCord(String keys) {
        return StringUtils.startsWith(keys, "cord=");
    }

    private String toKeys(String keys) {
        if(isKeyCord(keys)) {
            keys = keys.substring(5);

            String[] items = keys.split("(?<!\\\\)\\|");
            CharSequence[] charSequences = new CharSequence[items.length];
            for(int i = 0; i < items.length;i++) {
                String item = items[i];
                try {
                    charSequences[i] = Keys.valueOf(item);
                } catch (IllegalArgumentException e) {
                    charSequences[i] = item;
                }
            }

            return Keys.chord(charSequences);
        }

        return keys;
    }

    public void actionKeyDown(Keys keys) {
        LOG.keywordAppender().appendArgument("Keys", keys.name());

        Validate.notNull(actions, "actions is not yet started.");

        actions.keyDown(keys);
    }

    public void actionKeyDown(String locator, Keys keys) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Keys", keys.name());

        Validate.notNull(actions, "actions is not yet started.");

        WebElement src = finder.find(locator);

        if (src == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.keyDown(src, keys);
    }

    public void actionKeyUp(Keys keys) {
        LOG.keywordAppender().appendArgument("Keys", keys.name());

        Validate.notNull(actions, "actions is not yet started.");

        actions.keyUp(keys);
    }

    public void actionKeyUp(String locator, Keys keys) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Keys", keys.name());

        Validate.notNull(actions, "actions is not yet started.");

        WebElement src = finder.find(locator);

        if (src == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        actions.keyUp(src, keys);
    }

    public void actionDragAndDropBy(String srclocator, int xOffset, int yOffset) {
        LOG.keywordAppender()
                .appendLocator(srclocator)
                .appendArgument("xOffset", xOffset)
                .appendArgument("yOffset", yOffset);

        Validate.notNull(actions, "actions is not yet started.");

        WebElement src = finder.find(srclocator);

        if (src == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", srclocator));
        }

        actions.dragAndDropBy(src, xOffset, yOffset);
    }

    public void actionPause(int millis) {
        LOG.keywordAppender().appendArgument("Millis", millis);

        Validate.notNull(actions, "actions is not yet started.");

        actions.pause(millis);
    }

    public void actionPerform() {
        Validate.notNull(actions, "actions is not yet started.");

        actions.perform();
        actions = null;
    }

    public void mouseOver(String locator) throws IOException {
        String code = "var evObj = document.createEvent('MouseEvents');"
                + "evObj.initEvent( 'mouseover', true, true );"
                + "arguments[0].dispatchEvent(evObj);";
        executeJavascript(locator, code);
    }

    public void mouseUp(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).clickAndHold(el).release().perform();
    }

    public void pageShouldContain(String text) {
        LOG.keywordAppender().appendArgument("text", text);

        if (pageContains(text)) {
            LOG.info(String.format("Current page contains text '%s'.", text));
        } else {
            throw new AssertionError(String.format("Page should have contained text %s but did not.", text));
        }
    }

    public void pageShouldContainButton(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator, "button")) {
            if(!isElementPresent(locator, "input")) {
                throw new AssertionError(String.format("Page should have contained button '%s' but did not", locator));
            }
        }

        LOG.info(String.format("Current page contains button '%s'.", locator));
    }

    public void pageShouldContainCheckbox(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator, "input", "type", "checkbox")) {
            throw new AssertionError(String.format("Page should have contained checkbox '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains checkbox '%s'.", locator));
    }

    public void pageShouldContainElement(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator)) {
            throw new AssertionError(String.format("Page should have contained element '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains element '%s'.", locator));
    }

    public void pageShouldContainImage(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator,"img")) {
            throw new AssertionError(String.format("Page should have contained image '%s' but did not.", locator));
        }

        LOG.info(String.format("Current page contains image '%s'.", locator));
    }

    public void pageShouldContainLink(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator,"a")) {
            throw new AssertionError("Page should have contained link but did not.");
        }

        LOG.info(String.format("Current page contains link '%s'.", locator));
    }

    public void pageShouldContainList(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator,"select")) {
            throw new AssertionError(String.format("Page should have contained list '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains list '%s'.", locator));
    }

    public void pageShouldContainRadio(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator, "input", "type", "radio")) {
            throw new AssertionError(String.format("Page should have contained radio '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains radio '%s'.", locator));
    }

    public void pageShouldContainTextfield(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator, "input", "type", "text")) {
            throw new AssertionError(String.format("Page should have contained textfield '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains textfield '%s'.", locator));
    }

    public void pageShouldContainPassword(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(!isElementPresent(locator, "input", "type", "password")) {
            throw new AssertionError(String.format("Page should have contained password field '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains password field '%s'.", locator));
    }

    public void pageShouldNotContain(String text) {
        LOG.keywordAppender().appendArgument("Text", text);

        if (pageContains(text)) {
            throw new AssertionError(String.format("Page should not have contained text %s but did.", text));
        } else {
            LOG.info(String.format("Current page contains text '%s'.", text));
        }
    }

    public void pageShouldNotContainButton(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator, "button") || isElementPresent(locator, "input")) {
            throw new AssertionError(String.format("Page should not have contained button '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains button '%s'.", locator));
    }

    public void pageShouldNotContainCheckbox(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator, "input", "type", "checkbox")) {
            throw new AssertionError(String.format("Page should not have contained checkbox '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains checkbox '%s'.", locator));
    }

    public void pageShouldNotContainElement(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator)) {
            throw new AssertionError(String.format("Page should not have contained element '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains element '%s'.", locator));
    }

    public void pageShouldNotContainImage(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator,"img")) {
            throw new AssertionError(String.format("Page should not have contained image '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains image '%s'.", locator));
    }

    public void pageShouldNotContainLink(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator,"a")) {
            throw new AssertionError(String.format("Page should not have contained link '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains link '%s'.", locator));
    }

    public void pageShouldNotContainList(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator,"select")) {
            throw new AssertionError(String.format("Page should not have contained list '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains list '%s'.", locator));
    }

    public void pageShouldNotContainRadio(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator, "input", "type", "radio")) {
            throw new AssertionError(String.format("Page should not have contained radio '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains radio '%s'.", locator));
    }

    public void pageShouldNotContainTextfield(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator, "input", "type", "text")) {
            throw new AssertionError(String.format("Page should not have contained textfield '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains textfield '%s'.", locator));
    }

    public void pageShouldNotContainPassword(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        if(isElementPresent(locator, "input", "type", "password")) {
            throw new AssertionError(String.format("Page should not have contained password field '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains password field '%s'.", locator));
    }

    public void pressKey(String locator, String key) {
        LOG.keywordAppender()
                .appendArgument("Key", key)
                .appendLocator(locator);

        if (key.startsWith("\\") && key.length() > 1) {
            int keyCode = Integer.valueOf(key.substring(2));
            key = mapAsciiKeyCodeToKey(keyCode) ;
            LOG.keywordAppender().appendArgument("Ascii Key Code", key);
        }

        if (key.length() < 1) {
            throw new IllegalArgumentException(String.format("Key value '%s' is invalid.", key));
        }

        WebElement el = finder.find(locator);
        el.sendKeys(key);
    }

    public void reloadPage() {
        driver.navigate().refresh();
    }

    public void radioButtonShouldBeSetTo(String groupName, String valueSelected) {
        LOG.keywordAppender()
                .appendArgument("Group Name", groupName)
                .appendArgument("Value Selected", valueSelected);

        List<WebElement> els = getRadioButton(groupName);
        String actualValueSelected = getValueFromRadioButtons(els);

        if (actualValueSelected == null || !actualValueSelected.equalsIgnoreCase(valueSelected)) {
            throw new AssertionError(String.format("Selection of radio button '%s' should have been '%s' but was '%s'", groupName, valueSelected, actualValueSelected));
        }
    }

    public void radioButtonShouldNotBeSelected(String groupName) {
        LOG.keywordAppender().appendArgument("Group Name", groupName);

        List<WebElement> els = getRadioButton(groupName);
        String actualValue = getValueFromRadioButtons(els);

        if (actualValue != null) {
            throw new AssertionError(String.format("Radio button group '%s' should not have had selection, but '%s' was selected", groupName, actualValue));
        }
    }

    public void scrollToElement(String locator) throws InterruptedException {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);
        executor.executeScript("arguments[0].scrollIntoView(true);", el);
        Thread.sleep(DEFAULT_POLL_MILLIS);
    }

    public void selectRadioButton(String groupName, String value) {
        LOG.keywordAppender()
                .appendArgument("Group Name", groupName)
                .appendArgument("Value", value);

        WebElement el = getRadioButtonWithValue(groupName, value);
        if (!el.isSelected()) {
            el.click();
        }
    }
    
    public boolean isElementSelected(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, true, "input");
        return el.isSelected();
    }

    public void selectCheckbox(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, true, "input");
        if (!el.isSelected()) {
            el.click();
        }
    }

    public void unselectCheckbox(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = getCheckbox(locator);
        if (el.isSelected()) {
            el.click();
        }
    }

    public void selectFrame(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);
        driver.switchTo().frame(el);
    }

    public void unselectFrame() {
        driver.switchTo().defaultContent();
    }

    public List<String> getWindowHandles() {
        List<String> handles = new ArrayList<String>(driver.getWindowHandles());

        LOG.keywordAppender() .appendArgument("Handles", handles);

        return handles;
    }

    public String getWindowHandle() {
        String handle = driver.getWindowHandle();

        LOG.keywordAppender().appendArgument("Handle", handle);

        return handle;
    }

    public void selectWindow(String windowName) {
        LOG.keywordAppender().appendArgument("Handle", windowName);

        driver.switchTo().window(windowName);
    }

    public void submitForm(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator, "form");
        el.submit();
    }

    public void textfieldValueShouldBe(String locator, String expectedValue) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Expected", expectedValue);

        WebElement el = finder.find(locator,"input");

        String actual = null;
        if (el == null) {
            el = finder.find(locator,"textarea");
        }

        if (el != null) {
            actual = el.getAttribute("value");
        }

        LOG.keywordAppender().appendArgument("Actual", actual);

        if (!StringUtils.equalsIgnoreCase(actual, expectedValue)) {
            throw new AssertionError(String.format("Value of text field '%s' should have been '%s' but was '%s'", locator, expectedValue, actual));
        }

        LOG.info(String.format("Content of text field '%s' is '%s'.", locator, expectedValue));
    }

    public void titleShouldBe(String title) {
        LOG.keywordAppender()
                .appendArgument("Expected", title)
                .appendArgument("Actual", driver.getTitle());

        if (!driver.getTitle().equalsIgnoreCase(title)) {
            throw new AssertionError(String.format("Title should have been '%s' but was '%s'", title, driver.getTitle()));
        }

        LOG.keywordAppender().appendArgument("Title", title);
    }

    public void delay(long millis) {
        LOG.keywordAppender().appendArgument("Delay Millis", millis);

        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public boolean waitForJavaScriptCondition(final String javaScript, int timeOutInSeconds) {
        boolean jscondition;

        LOG.keywordAppender()
                .appendJavascript(javaScript)
                .appendArgument("timeOutInSeconds", timeOutInSeconds);
        
        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            (new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
                }
            });
            jscondition =  (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript);
            driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS); //reset implicitlyWait
                        
            return jscondition;
        } catch (Exception e) {
            String.format("timeout (%d s) reached.",  timeOutInSeconds);
        }
        
        return false;
    }
    
    public boolean waitForJQueryProcessing(int timeOutInSeconds){
        LOG.keywordAppender().appendArgument("timeOutInSeconds", timeOutInSeconds);

        boolean jQcondition = false;

        try{
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()
            (new WebDriverWait(driver, timeOutInSeconds)).until(new ExpectedCondition<Boolean>() {
                @Override
                public Boolean apply(WebDriver driverObject) {
                    return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
                }
            });
            jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
            driver.manage().timeouts().implicitlyWait(implicitWaitInSeconds, TimeUnit.SECONDS); //reset implicitlyWait
            
            return jQcondition;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jQcondition;
    }

    public void waitTillElementContainsRegex(String locator, String regex) {
        waitTillElementContainsRegex(locator, regex, DEFAULT_POLL_MILLIS, TimeUnit.SECONDS.toMillis(implicitWaitInSeconds));
    }

    public void waitTillElementContainsRegex(String locator, String regex, long pollMillis, long timeoutMillis) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("Regex", regex)
                .appendArgument("pollMillis", pollMillis)
                .appendArgument("timeoutMillis", timeoutMillis);

        long start = System.currentTimeMillis();
        long elapse = -1;
        WebElement el;

        do {
            if(elapse != -1) {
                try {
                    Thread.sleep(pollMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            el = finder.find(locator, false);
            elapse = System.currentTimeMillis() - start;

            // only set el to none null if text is found.
            if(el != null) {
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(el.getText());

                if(!matcher.find()) {
                    el = null;
                }
            }
        } while(el == null && elapse < timeoutMillis);

        if(el == null) {
            throw new IllegalStateException(String.format("timeout for locating '%s' (%d ms) reached.", locator, timeoutMillis));
        }
    }

    public void waitTillElementContainsText(String locator, String text) {
        waitTillElementContainsRegex(locator, text, DEFAULT_POLL_MILLIS, TimeUnit.SECONDS.toMillis(implicitWaitInSeconds));
    }

    public void waitTillElementContainsText(String locator, String text, long pollMillis, long timeoutMillis) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("text", text)
                .appendArgument("pollMillis", pollMillis)
                .appendArgument("timeoutMillis", timeoutMillis);

        long start = System.currentTimeMillis();
        long elapse = -1;
        WebElement el;

        do {
            if(elapse != -1) {
                try {
                    Thread.sleep(pollMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            el = finder.find(locator, false);
            elapse = System.currentTimeMillis() - start;

            // only set el to none null if text is found.
            if(el != null && !StringUtils.contains(el.getText(), text)) {
                el = null;
            }
        } while(el == null && elapse < timeoutMillis);

        if(el == null) {
            throw new IllegalStateException(String.format("timeout for locating '%s' (%d ms) reached.", locator, timeoutMillis));
        }
    }

    public void waitTillElementFound(String locator) {
        waitTillElementVisible(locator, DEFAULT_POLL_MILLIS, TimeUnit.SECONDS.toMillis(implicitWaitInSeconds));
    }

    public void waitTillElementFound(String locator, long pollMillis, long timeoutMillis) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("pollMillis", pollMillis)
                .appendArgument("timeoutMillis", timeoutMillis);

        long start = System.currentTimeMillis();
        long elapse = -1;
        WebElement el;

        do {
            if(elapse != -1) {
                try {
                    Thread.sleep(pollMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            el = finder.find(locator, false);
            elapse = System.currentTimeMillis() - start;
        } while(el == null && elapse < timeoutMillis);

        if(el == null) {
            throw new IllegalStateException(String.format("timeout for locating '%s' (%d ms) reached.", locator, timeoutMillis));
        }
    }

    public void waitTillElementVisible(String locator) {
        waitTillElementVisible(locator, DEFAULT_POLL_MILLIS, TimeUnit.SECONDS.toMillis(implicitWaitInSeconds));
    }

    public void waitTillElementVisible(String locator, long pollMillis, long timeoutMillis) {
        LOG.keywordAppender()
                .appendLocator(locator)
                .appendArgument("pollMillis", pollMillis)
                .appendArgument("timeoutMillis", timeoutMillis);

        long start = System.currentTimeMillis();
        long elapse = -1;
        WebElement el;

        do {
            if(elapse != -1) {
                try {
                    Thread.sleep(pollMillis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            el = finder.find(locator, false);
            if(el == null) {
                throw new IllegalStateException(String.format("No element found with locator '%s'.", locator));
            }

            elapse = System.currentTimeMillis() - start;
        } while(!el.isDisplayed() && elapse < timeoutMillis);

        if(!el.isDisplayed()) {
            throw new IllegalStateException(String.format("timeout for locating '%s' (%d ms) reached.", locator, timeoutMillis));
        }
    }

    public void cssShouldMatchXTimes(String cssLocator, int count) {
        LOG.keywordAppender().appendArgument("Expected Count", count);

        int actual = getMatchingCSSCount(cssLocator);
        if(actual != count) {
            throw new AssertionError(String.format("Matching css count for %s expected is %d, but was %d.", cssLocator, count, actual));
        }
    }

    public void zoomIn(int times) {
        LOG.keywordAppender().appendArgument("Times", times);

        WebElement html = driver.findElement(By.tagName("html"));

        for(int i = 0; i < times; i++) {
            if(isMacOS()) {
                LOG.keywordAppender().appendArgument("Os X", true);

                html.sendKeys(Keys.chord(Keys.COMMAND, Keys.ADD));
            } else {
                html.sendKeys(Keys.chord(Keys.CONTROL, Keys.ADD));
            }
        }
    }

    public void zoomOut(int times) {
        LOG.keywordAppender().appendArgument("Times", times);

        WebElement html = driver.findElement(By.tagName("html"));

        for(int i = 0; i < times; i++) {
            if(isMacOS()) {
                LOG.keywordAppender().appendArgument("Os X", true);

                html.sendKeys(Keys.chord(Keys.COMMAND, Keys.SUBTRACT));
            } else {
                html.sendKeys(Keys.chord(Keys.CONTROL, Keys.SUBTRACT));
            }
        }
    }

    public boolean isMacOS() {
        String os = StringUtils.lowerCase(System.getProperty("os.name", "generic"));

        return os.contains("mac") || os.contains("darwin");
    }

    public void xpathShouldMatchXTimes(String xpath, int count) {
        LOG.keywordAppender().appendArgument("Expected Count", count);

        int actual = getMatchingXPathCount(xpath);
        if(actual != count) {
            throw new AssertionError(String.format("Matching xpath count for %s expected is %d, but was %d.", xpath, count, actual));
        }
    }

    private String closeAlert() {
        return closeAlert(false);
    }

    private String closeAlert(boolean confirm) {
        Alert alert = driver.switchTo().alert();
        String text = alert.getText();

        if (!confirm) {
            alert.dismiss();
        } else {
            alert.accept();
        }

        return text;
    }

    public boolean isAlertPresent() {
        try {
            Alert alert = driver.switchTo().alert();

            LOG.keywordAppender().appendArgument("Message", alert.getText());

            return true;
        } catch (NoAlertPresentException ex) {
            LOG.info("Did not find any alerts.");
            return false;
        }
    }

    public boolean isTextPresent(String text) {
        LOG.keywordAppender().appendArgument("Text", text);

        return driver.findElement(By.cssSelector("BODY")).getText().matches("^[\\s\\S]*" + text + "[\\s\\S]*$");
    }
    
    public boolean isTextPresentInPageSource(String html, String text) {
        LOG.keywordAppender().appendArgument("Text", text);

        return html.matches("^[\\s\\S]*" + text + "[\\s\\S]*$");
    }
    
    private File newScreenCaptureFile() {
        String name = String.format("screen_capture_%d_%d.png", screenCaptureSeed, ++screenCaptureCtr);

        return new File(screenCaptureDir, name);
    }

    private boolean frameContains(String locator, String text) {
        WebElement el = finder.find(locator);
        driver.switchTo().frame(el);
        LOG.info(String.format("Searching for text from frame '%s'.", locator));
        boolean found = textIsPresent(text);
        driver.switchTo().defaultContent();
        return found;
    }

    private WebElement getCheckbox(String locator){
        return finder.find(locator, "input");
    }

    private List<WebElement> getRadioButton(String groupName) {
        String xpath = String.format("//input[@type='radio' and @name='%s']", groupName);
        LOG.info("Radio group locator: " + xpath);

        return driver.findElements(By.xpath(xpath));
    }

    private String getValueFromRadioButtons(List<WebElement> els) {
        for (WebElement el : els) {
            if (el.isSelected()) {
                return el.getAttribute("value");
            }
        }

        return null;
    }

    private WebElement getRadioButtonWithValue(String groupName, String value) {
        String xpath = String.format("//input[@type='radio' and @name='%s' and (@value='%s' or @id='%s')]", groupName, value, value);
        LOG.info("Radio group locator: " + xpath);

        return ElementFinder.findByXpath(driver, xpath, null, null);
    }

    /*
    private WebElement getCheckboxWithValue(String groupName, String value) {
        String xpath = String.format("//input[@type='checkbox' and @name='%s' and (@value='%s' or @id='%s')]", groupName, value, value);
        LOG.info("Checkbox group locator: " + xpath);

        return ElementFinder.findByXpath(driver,xpath,null,null);
    }*/

    private boolean textIsPresent(String text) {
        String locator = String.format("xpath=//*[contains(., %s)]", escapeXpathValue(text));
        return finder.find(locator, false) != null;
    }

    private String escapeXpathValue(String value) {
        // "I'm here!"
        // -> concat('"I', "'", 'm here!"')
        // -> '"I' + "'" + 'm here!"'
        if (StringUtils.contains(value,'"') && StringUtils.contains(value,'\'')) {
            String [] parts_wo_apos = StringUtils.split(value,'\'');
            return String.format("concat('%s')", StringUtils.join(parts_wo_apos, "', \"'\", '"));
        }

        // I'm Lucky
        // -> "I'm Lucky"
        if(StringUtils.contains(value, "\'")) {
            return String.format("\"%s\"", value);
        }

        // Hi there
        // -> 'Hi There'
        return String.format("'%s'", value);
    }

    private void deleteCookie(Cookie cookie) {
        LOG.keywordAppender().appendArgument(cookie.getName(), cookie.getValue());

        driver.manage().deleteCookie(cookie);
    }

    private boolean isEnabled(String locator) {
        WebElement el = finder.find(locator);

        if (!isFormElement(el)) {
            throw new AssertionError(String.format("Element %s is not an input.", locator));
        }

        if (!el.isEnabled()) {
            return false;
        }
        
        String readOnly = el.getAttribute("readonly");
        
        if (readOnly != null && (readOnly.equalsIgnoreCase("readonly") || readOnly.equalsIgnoreCase("true"))) {
            return false;
        }

        return true;
    }

    private boolean isFormElement(WebElement element) {
        if (element == null) {
            return false;
        }

        String tagName = element.getTagName().toLowerCase();
        return (tagName.equalsIgnoreCase("input") || tagName.equalsIgnoreCase("select") || tagName.equalsIgnoreCase("textarea") || tagName.equalsIgnoreCase("button"));
    }

    private boolean isVisible(String locator) {
        WebElement el = finder.find(locator);

        if (el == null) {
            throw new AssertionError(String.format("Locator %s not found.", locator));
        }

        return el.isDisplayed();
    }

    private String[] parseAttributeLocator(String attributeLocator) {
        String[] parts = attributeLocator.split("@");
        LOG.keywordAppender().appendArgument("Attribute Locator Parts", Arrays.asList(parts));
        LOG.keywordAppender().appendArgument("Attribute Locator Size", parts.length);

        if (parts.length == 0  || parts.length < 2) {
            throw new IllegalArgumentException(String.format("Invalid attribute locator '%s'", attributeLocator));
        }
        if (StringUtils.isEmpty(parts[0])) {
            throw new IllegalStateException(String.format("Attribute locator '%s' does not contain an element locator", attributeLocator));
        }

        if (StringUtils.isEmpty(parts[1])) {
            throw new IllegalStateException(String.format("Attribute locator '%s' does not contain an attribute name", attributeLocator));
        }

        return new String[] {parts[0],parts[1]};
    }

    public String getInnerHtml(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);

        return (String) executor.executeScript("return arguments[0].innerHTML;", el);
    }

    public void focus(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        WebElement el = finder.find(locator);
        executor.executeScript("arguments[0].focus();", el);
    }

    private String getText(String locator, boolean validateResult) {
        WebElement el = finder.find(locator,validateResult);
        if (el != null) {
            return el.getText();
        }
        return null;
    }

    private void inputTextIntoTextField(String locator, String text) {
        WebElement el = finder.find(locator);
        el.clear();
        el.sendKeys(toKeys(text));
    }

    private boolean pageContains(String text) {
        driver.switchTo().defaultContent();

        if(textIsPresent(text)) {
            return true;
        }

        List<WebElement> frames = driver.findElements(By.xpath("//frame|//iframe"));

        for(WebElement frame : frames) {
            driver.switchTo().frame(frame);

            if(textIsPresent(text)) {
                return true;
            }

            driver.switchTo().defaultContent();
        }

        return false;
    }

    private boolean isElementPresent(String locator, String tagName, String attrName, String attrValue) {
        return finder.find(locator, false, tagName, attrName, attrValue) != null;
    }

    private boolean isElementPresent(String locator, String tagName) {
        return finder.find(locator, false, tagName) != null;
    }

    private boolean isElementPresent(String locator) {
        return finder.find(locator, false) != null;
    }

    private WebElement getSelectList(String locator) {
        LOG.keywordAppender().appendLocator(locator);

        return finder.find(locator, true, "select");
    }

    private List<WebElement> getSelectListOptions(String locator) {
        WebElement selectEl = getSelectList(locator);
        Select select = new Select(selectEl);

        return select.getOptions();
    }

    private List<WebElement> getSelectListOptions(WebElement selectEl) {
        Select select = new Select(selectEl);

        return select.getOptions();
    }

    private List<String> getLabelsForOptions(List<WebElement> selectOptions) {
        List<String> labels = new ArrayList<String>();
        for (WebElement option : selectOptions) {
            labels.add(option.getText());
        }
        return labels;
    }

    private List<WebElement> getSelectListOptionsSelected(String locator) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        List<WebElement> selectedOptions = new ArrayList<WebElement>();
        for (WebElement selected: selectOptions) {
            if (selected.isSelected()) {
                selectedOptions.add(selected);
            }
        }

        return selectedOptions;
    }

    private List<String> getValuesForOptions(List<WebElement> selectedOptions) {
        List<String> values = new ArrayList<String>();
        for (WebElement option : selectedOptions) {
            values.add(option.getAttribute("value"));
        }
        return values;
    }

    private boolean isMultiSelectList(WebElement el) {
        String multipleValue = el.getAttribute("multiple");

        return multipleValue != null && (multipleValue.equalsIgnoreCase("true") || multipleValue.equalsIgnoreCase("multiple"));
    }

    private String mapAsciiKeyCodeToKey (int keyCode) {
        Map<Integer, Keys> keysMap = new HashMap<Integer, Keys>();
        keysMap.put(0,Keys.NULL);
        keysMap.put(8,Keys.BACK_SPACE);
        keysMap.put(9,Keys.TAB);
        keysMap.put(10,Keys.RETURN);
        keysMap.put(13,Keys.ENTER);
        keysMap.put(24,Keys.CANCEL);
        keysMap.put(27,Keys.ESCAPE);
        keysMap.put(32,Keys.SPACE);
        keysMap.put(42,Keys.MULTIPLY);
        keysMap.put(43,Keys.ADD);
        keysMap.put(44,Keys.SUBTRACT);
        keysMap.put(56,Keys.DECIMAL);
        keysMap.put(57,Keys.DIVIDE);
        keysMap.put(59,Keys.SEMICOLON);
        keysMap.put(61,Keys.EQUALS);
        keysMap.put(127,Keys.DELETE);
        Keys key = keysMap.get(keyCode);

        if (key == null) {
            Character c = (char) keyCode;
            return c.toString();
        }

        return key.toString();
    }
}
