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
import org.jspringbot.syntax.HighlightRobotLogger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SeleniumHelper {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(SeleniumHelper.class);

    protected WebDriver driver;

    protected ElementFinder finder;

    protected JavascriptExecutor executor;

    protected boolean cancelOnNextConfirmation = false;

    protected int screenCaptureCtr = 0;

    protected long screenCaptureSeed = System.currentTimeMillis();

    protected File screenCaptureDir;
    
    protected int DEFAULT_WAIT_4_PAGE = 12; 

    public SeleniumHelper() {}

    public SeleniumHelper(WebDriver driver) {
        this.driver = driver;
        this.executor = (JavascriptExecutor) driver;
        this.finder = new ElementFinder(driver);
    }

    public void windowMaximize() {
        driver.manage().window().maximize();
    }

    public void assignIdToElement(String id, String locator) {
        LOG.createAppender()
                .appendBold("Assign ID To Element:")
                .appendCss(locator)
                .appendProperty("id", id)
                .log();

        WebElement el = finder.find(locator);

        executor.executeScript(String.format("arguments[0].id = '%s';", id), el);
    }

    public Boolean findElement(String locator){
        if(!isElementPresent(locator)) {
            LOG.info(String.format("Page should have contained element '%s' but did not", locator));
            return false;
        }
        
        LOG.info(String.format("element '%s' found on page", locator));
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

        LOG.createAppender()
                .appendBold("Navigate To:")
                .appendProperty("link", url)
                .log();
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

        LOG.createAppender()
                .appendBold("Alert Should Be Present:")
                .appendProperty("Actual Alert Text", actualAlertText)
                .appendProperty("Expected Alert Text", expectedAlertText)
                .log();

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

        LOG.createAppender()
                .appendBold("Checkbox Should Be Selected:")
                .appendCss(locator)
                .appendProperty("Selected", el.isSelected())
                .log();

        if (!el.isSelected()) {
            throw new IllegalArgumentException("Checkbox should have been selected.");
        }
    }

    public void checkboxShouldNotBeSelected(String locator) {
        WebElement el = getCheckbox(locator);

        LOG.createAppender()
                .appendBold("Checkbox Should Not e Selected:")
                .appendCss(locator)
                .appendProperty("Selected", el.isSelected())
                .log();

        if (el.isSelected()) {
            throw new IllegalArgumentException("Checkbox should not have been selected.");
        }
    }

    public void chooseFile(String locator, String filePath) {
        File file = new File(filePath);

        LOG.createAppender()
                .appendBold("Choose File:")
                .appendCss(locator)
                .appendProperty("File Path", filePath)
                .appendProperty("Is File", file.isFile())
                .log();

        if (!file.isFile()) {
            throw new IllegalArgumentException("File does not exist on the local file system.");
        }

        WebElement el = finder.find(locator);
        el.sendKeys(filePath);
    }

    public void clickButton(String locator) {
        LOG.createAppender()
                .appendBold("Click Button:")
                .appendCss(locator)
                .log();

        WebElement button = finder.find(locator, false, "input");
        if (button == null) {
            button = finder.find(locator, "button");
        }
        button.click();
    }

    public void clickElement(String locator) {
        LOG.createAppender()
                .appendBold("Click Element:")
                .appendCss(locator)
                .log();

        finder.find(locator).click();
    }

    public void clickImage(String locator) {
        LOG.createAppender()
                .appendBold("Click Image:")
                .appendCss(locator)
                .log();

        WebElement image = finder.find(locator, false, "img");

        if (image == null) {
            image = finder.find(locator,"input");
        }

        image.click();
    }

    public void clickLink(String locator) {
        LOG.createAppender()
                .appendBold("Click Link:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);
        el.click();
    }

    public List<String> getAllLinks() {
        List<String> links = new ArrayList<String>();

        WebElement el = finder.find("tag=a",false,"a");
        links.add(el.getAttribute("id"));

        return links;
    }

    public void captureScreenShot() throws IOException {
        byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);

        FileOutputStream out = null;
        try {
            File file = newScreenCaptureFile();

            LOG.html("Screen captured (%d): <br /> <img src='%s'/>", screenCaptureCtr, file.getName());

            out = new FileOutputStream(file);
            IOUtils.write(bytes, out);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public void deleteAllCookies() {
        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender()
                .appendBold("Delete All Cookies:");

        Set<Cookie> allCookies = getCookies();
        for (Cookie loadedCookie : allCookies) {
            appender.appendProperty(loadedCookie.getName(), loadedCookie.getValue());
            deleteCookie(loadedCookie);
        }

        appender.log();
    }

    public void deleteCookie(String cookieName) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);

        LOG.createAppender()
                .appendBold("Delete Cookie:")
                .appendProperty(cookie.getName(), cookie.getValue())
                .log();

        driver.manage().deleteCookie(cookie);
    }

    public String getCookieValue (String cookieName) {
        Cookie cookie = driver.manage().getCookieNamed(cookieName);


        if (cookie != null) {
            LOG.createAppender()
                    .appendBold("Get Cookie Value")
                    .appendProperty(cookie.getName(), cookie.getValue())
                    .log();

            return cookie.getValue();
        } else {
            throw new IllegalStateException(String.format("Cookie with name '%s' not found", cookieName));
        }
    }

    public Set<Cookie> getCookies () {
        Set<Cookie> cookies = driver.manage().getCookies();

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender()
                .appendBold("Get Cookies:");

        for(Cookie cookie : cookies) {
            appender.appendProperty(cookie.getName(), cookie.getValue());
        }

        appender.log();

        return cookies;
    }

    public void doubleClickElement(String locator) {
        LOG.createAppender()
                .appendBold("Double Click Element:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);
        new Actions(driver).doubleClick(el).perform();
    }

    public void dragAndDrop(String locatorSrc, String locatorDest) {
        LOG.createAppender()
                .appendBold("Drag And Drop:")
                .append("<br/>Source:")
                .appendCss(locatorSrc)
                .append("Destination:")
                .appendCss(locatorDest)
                .log();

        WebElement element = finder.find(locatorSrc);
        WebElement target = finder.find(locatorDest);

        new Actions(driver).dragAndDrop(element, target).perform();
    }

    public void dragAndDropByOffset(String locatorSrc, int xOffset, int yOffset) {

        LOG.createAppender()
                .appendBold("Drag And Drop By Offset:")
                .appendCss(locatorSrc)
                .appendProperty("xOffset", xOffset)
                .appendProperty("yOffset", yOffset)
                .log();

        WebElement element = finder.find(locatorSrc);

        new Actions(driver).dragAndDropBy(element, xOffset, yOffset).perform();
    }

    public void elementShouldBeDisabled(String locator) {

        LOG.createAppender()
                .appendBold("Element Should Be Disabled:")
                .appendCss(locator)
                .appendProperty("Enabled", isEnabled(locator))
                .log();

        if (isEnabled(locator))  {
            throw new AssertionError("Element is enabled.");
        }
    }

    public void elementShouldBeEnabled(String locator) {
        boolean isEnabled = isEnabled(locator);

        LOG.createAppender()
                .appendBold("Element Should Be Enabled:")
                .appendCss(locator)
                .appendProperty("Enabled", isEnabled)
                .log();

        if (!isEnabled)  {
            throw new AssertionError("Element is disabled");
        }
    }

    public void elementShouldBeVisible(String locator) {
        boolean isVisible = isVisible(locator);
        LOG.createAppender()
                .appendBold("Element Should Be Visible:")
                .appendCss(locator)
                .appendProperty("Visible", isVisible)
                .log();

        if (!isVisible) {
            throw new AssertionError("The element should be visible, but it is not.");
        }
    }
    
    public boolean isElementVisible(String locator) {
    	boolean isVisible = isVisible(locator);
        LOG.createAppender()
                .appendBold("Is Element Visible:")
                .appendCss(locator)
                .appendProperty("Visible", isVisible)
                .log();
        return isVisible;
    }

    public void elementShouldNotBeVisible(String locator) {
        boolean isVisible = isVisible(locator);

        LOG.createAppender()
                .appendBold("Element Should Not Be Visible:")
                .appendCss(locator)
                .appendProperty("Visible", isVisible)
                .log();

        if (isVisible) {
            throw new AssertionError("The element should not be visible, but it is not.");
        }
    }

    public void currentFrameShouldContain(String text) {
        boolean textIsPresent = textIsPresent(text);

        LOG.createAppender()
                .appendBold("Current Frame Should Contain Text:")
                .appendProperty("text", text)
                .appendProperty("Text Is Present", textIsPresent)
                .log();

        if (!textIsPresent) {
            throw new AssertionError("Page should have contained text but did not.");
        }
    }

    public void frameShouldContainText(String locator, String text) {
        boolean frameContains = frameContains(locator, text);
        LOG.createAppender()
                .appendBold("Frame Should Contain Text:")
                .appendProperty("text", text)
                .appendProperty("Text Is Present", frameContains)
                .log();


        if (!frameContains) {
            throw new AssertionError("Page should have contained text but did not.");
        }
    }

    public void elementShouldContain(String locator, String expected) {
        String actual = getText(locator, false);

        LOG.createAppender()
                .appendBold("Element Should Contain Text:")
                .appendCss(locator)
                .appendProperty("Actual", actual)
                .appendProperty("Expected", expected)
                .log();

        if (!expected.equalsIgnoreCase(actual)) {
            throw new AssertionError("Element should have contained text.");
        }
    }

    public void elementShouldContainClass(String locator, String expectedClassName) {
        WebElement el = finder.find(locator, true);

        String classNames = el.getAttribute("class");

        LOG.createAppender()
                .appendBold("Element Should Contain Class:")
                .appendCss(locator)
                .appendProperty("Class Names", classNames)
                .appendProperty("Expected Class Name", expectedClassName)
                .log();

        if(StringUtils.isNotEmpty(classNames)) {
            if(Arrays.asList(StringUtils.split(classNames, " ")).contains(expectedClassName)) {
                return;
            }
        }

        throw new AssertionError("Element should have contained class.");
    }

    public void elementShouldContainType(String locator, String expectedTypeName) {
        WebElement el = finder.find(locator, true);

        String typeNames = el.getAttribute("type");

        LOG.createAppender()
                .appendBold("Element Should Contain Class:")
                .appendCss(locator)
                .appendProperty("Class Names", typeNames)
                .appendProperty("Expected Type Name", expectedTypeName)
                .log();

        if(StringUtils.isNotEmpty(typeNames)) {
            if(Arrays.asList(StringUtils.split(typeNames, " ")).contains(expectedTypeName)) {
                return;
            }
        }

        throw new AssertionError("Element should have contained type.");
    }

    public void elementShouldNotContainClass(String locator, String expectedClassName) {
        WebElement el = finder.find(locator, true);

        String classNames = el.getAttribute("class");

        LOG.createAppender()
                .appendBold("Element Should Not Contain Class:")
                .appendCss(locator)
                .appendProperty("Class Names", classNames)
                .appendProperty("Expected Class Name", expectedClassName)
                .log();

        if(StringUtils.isNotEmpty(classNames)) {
            if(Arrays.asList(StringUtils.split(classNames, " ")).contains(expectedClassName)) {
                throw new AssertionError("Element should have not contained class");
            }
        }
    }

    public void elementTextShouldBe(String locator, String expectedText) {
        WebElement el = finder.find(locator);

        String actualText = el.getText();

        LOG.createAppender()
                .appendBold("Element Text Should Be:")
                .appendCss(locator)
                .appendProperty("Actual Text", actualText)
                .appendProperty("Expected Text", expectedText)
                .log();

        if (!StringUtils.equals(StringUtils.trim(expectedText), StringUtils.trim(actualText))) {
            throw new AssertionError("The text of element is not as expected.");
        }
    }

    public Object executeJavascript(String code) throws IOException {
        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender()
                .appendBold("Execute Javascript:");

        if(StringUtils.startsWith(code, "file:") || StringUtils.startsWith(code, "classpath:")) {
            appender.appendProperty("Resource", code);
            ResourceEditor editor = new ResourceEditor();
            editor.setAsText(code);
            Resource resource = (Resource) editor.getValue();

            code = new String(IOUtils.toCharArray(resource.getInputStream()));
        }

        appender.appendJavascript(code);
        appender.log();

        return executor.executeScript(code);
    }

    public String getElementAttribute(String attributeLocator) {
        String[] parts = parseAttributeLocator(attributeLocator);
        String locator = parts[0];
        String attributeName = parts[1];

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender()
                .appendBold("Get Element Attribute:")
                .appendCss(locator)
                .appendProperty("Attribute Name", attributeName);

        WebElement el = finder.find(parts[0], true);

        if (el == null) {
            appender.log();
            throw new IllegalArgumentException("Element not found");
        }

        String attributeValue = el.getAttribute(attributeName);

        appender.appendProperty("Attribute Value", attributeValue)
                .log();

        return attributeValue;
    }

    public int getHorizontalPosition(String locator) {
        WebElement element = finder.find(locator);

        Point point = element.getLocation();

        LOG.createAppender()
                .appendBold("Get Horizontal Position:")
                .appendCss(locator)
                .appendProperty("X", point.getX())
                .appendProperty("Y", point.getY())
                .log();

        return point.getY();
    }

    public int getVerticalPosition(String locator) {
        WebElement element = finder.find(locator);

        Point point = element.getLocation();

        LOG.createAppender()
                .appendBold("Get Vertical Position:")
                .appendCss(locator)
                .appendProperty("X", point.getX())
                .appendProperty("Y", point.getY())
                .log();

        return point.getY();
    }

    public String getLocation() {
        LOG.createAppender()
                .appendBold("Get Location:")
                .appendProperty("Current URL", driver.getCurrentUrl())
                .log();

        return driver.getCurrentUrl();
    }

    public int getMatchingCSSCount(String cssLocator) {
        if(StringUtils.startsWith(cssLocator, "css=")) {
            cssLocator = cssLocator.substring(4);
        }

        int count = driver.findElements(By.cssSelector(cssLocator)).size();

        LOG.createAppender()
                .appendBold("Get Matching CSS Count:")
                .appendCss(cssLocator)
                .appendProperty("Count", count)
                .log();

        return count;
    }

    public int getMatchingXPathCount(String xpath) {
        if(StringUtils.startsWith(xpath, "xpath=")) {
            xpath = xpath.substring(6);
        }

        int count = driver.findElements(By.xpath(xpath)).size();

        LOG.createAppender()
                .appendBold("Get Matching XPath Count:")
                .appendCss(xpath)
                .appendProperty("Count", count)
                .log();

        return count;
    }

    public String getSource() {
        String source = driver.getPageSource();

        LOG.createAppender()
                .appendBold("Get Source:")
                .appendXML(source)
                .log();

        return source;
    }

    public List<String> getListItems(String locator) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        List<String> labels = getLabelsForOptions(selectOptions);

        LOG.createAppender()
                .appendBold("Get List Items:")
                .appendCss(locator)
                .appendProperty("Labels", labels)
                .log();

        return labels;
    }

    public String getSelectedListLabel (String locator) {
        List<String> selectedLabels = getSelectedListLabels(locator);

        if (selectedLabels.size() != 1) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have a single selected value", locator));
        }

        return selectedLabels.get(1);
    }

    public String getSelectedListValue(String locator) {
        List<String> selectedListValues = getSelectedListValues(locator);

        if (selectedListValues.size() != 1) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have a single selected value",locator));
        }

        return selectedListValues.get(1);
    }


    public List<String> getSelectedListValues(String locator) {
        List<WebElement> selectedOptions = getSelectListOptionsSelected(locator);

        if (selectedOptions.size() == 0) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have any selected values",locator));
        }

        return getValuesForOptions(selectedOptions);
    }

    public List<String> getSelectLabels(String locator) {
        List<WebElement> selectOptions = getSelectListOptions(locator);

        if (selectOptions.size() == 0) {
            throw new IllegalArgumentException(String.format("Select list with locator '%s' does not have any values",locator));
        }

        return getLabelsForOptions(selectOptions);
    }

    public List<String> getSelectedListLabels(String locator) {
        List<WebElement> selectedOptions = getSelectListOptionsSelected(locator);

        if (selectedOptions.size() == 0) {
            throw new IllegalArgumentException(String.format("Select list with locator %s does not have any selected values.", locator));
        }

        return getLabelsForOptions(selectedOptions);
    }

    public void selectAllFromList(String locator) {
        LOG.createAppender()
                .appendBold("Select All From List:")
                .appendCss(locator)
                .log();

        WebElement selectEl = getSelectList(locator);
        boolean isMultiSelectList = isMultiSelectList(selectEl);

        if (!isMultiSelectList) {
            LOG.createAppender()
                    .appendProperty("multi-select", isMultiSelectList)
                    .log();

            throw new IllegalArgumentException("Keyword 'Select all from list' works only for multi-select lists.");
        }

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender();

        List<WebElement> selectOptions = getSelectListOptions(selectEl);
        int index = 0;
        for(WebElement option : selectOptions) {
            if(!option.isSelected()) {
                appender.appendProperty(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText());
                option.click();
            } else {
                appender.appendProperty(String.format("option[index=%d,value=%s]", index++, option.getAttribute("value")), option.getText() + ": (already selected)");
            }
        }

        appender.log();
    }

    public String getText(String locator) {
        return getText(locator,true);
    }

    public String getTitle() {
        return driver.getTitle();
    }

    public String getValue(String locator){
        WebElement el = finder.find(locator);

        return el.getAttribute("value");
    }

    public void goBack() {
        driver.navigate().back();
    }

    public void inputPassword(String locator, String password) {
        LOG.info(String.format("Typing password into text field '%s'", locator));
        inputTextIntoTextField(locator, password);
    }

    public void inputText(String locator, String text) {
        LOG.info(String.format("Typing text '%s' into text field '%s'", text, locator));
        inputTextIntoTextField(locator, text);
    }

    public void listSelectionShouldBe(String locator, List<String> items) {
        List<String> values = getSelectedListValues(locator);

        boolean containsValues = items.containsAll(values);

        if(containsValues) {
            LOG.createAppender()
                    .appendBold("List Selection Should Be: (values)")
                    .appendCss(locator)
                    .appendProperty("Expected", items)
                    .appendProperty("Actual Values", values)
                    .log();

            return;
        }

        List<String> texts = getSelectedListLabels(locator);
        boolean containsText = items.containsAll(texts);

        if(containsText) {
            LOG.createAppender()
                    .appendBold("List Selection Should Be: (text)")
                    .appendCss(locator)
                    .appendProperty("Expected", items)
                    .appendProperty("Actual", texts)
                    .log();

            return;
        }

        throw new IllegalArgumentException("Selection list not found");
    }

    public void listValueSelectionShouldBe(String locator, List<String> items) {
        List<String> values = getSelectedListValues(locator);

        boolean containsValues = items.containsAll(values);

        if(containsValues) {
            LOG.createAppender()
                    .appendBold("List Value Selection Should Be:")
                    .appendCss(locator)
                    .appendProperty("Expected", items)
                    .appendProperty("Actual Values", values)
                    .log();

            return;
        }

        throw new IllegalArgumentException("Selection value for list not found");
    }

    public void listTextSelectionShouldBe(String locator, List<String> items) {
        List<String> texts = getSelectedListLabels(locator);
        boolean containsText = items.containsAll(texts);

        if(containsText) {
            LOG.createAppender()
                    .appendBold("List Selection Should Be: (text)")
                    .appendCss(locator)
                    .appendProperty("Expected", items)
                    .appendProperty("Actual", texts)
                    .log();

            return;
        }

        throw new IllegalArgumentException("Selection text list not found");
    }

    public void listShouldHaveNoSelection(String locator) {
        List<String> values = getSelectedListValues(locator);

        if(CollectionUtils.isNotEmpty(values)) {
            LOG.createAppender()
                    .appendBold("List Should Have No Selection:")
                    .appendCss(locator)
                    .appendProperty("Values", values)
                    .appendProperty("Texts", getSelectedListLabels(locator))
                    .log();

            throw new IllegalArgumentException("List should have no selection.");
        }

        LOG.createAppender()
                .appendBold("List Should Have No Selection:")
                .appendCss(locator)
                .log();
    }

    public void selectFromListByIndex(String locator, List<Integer> indices) {
        LOG.createAppender()
                .appendBold("Select From List By Index:")
                .appendCss(locator)
                .appendProperty("Indices", indices)
                .log();

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender();
        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            if(indices.contains(i)) {
                WebElement option = options.get(i);
                appender.append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(indices.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }

        appender.log();
    }

    public void selectFromListByValue(String locator, List<String> values) {
        LOG.createAppender()
                .appendBold("Select From List By Value:")
                .appendCss(locator)
                .appendProperty("Values", values)
                .log();

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender();
        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);

            if(values.contains(option.getAttribute("value"))) {
                appender.append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(values.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }

        appender.log();
    }

    public void selectFromListByLabel(String locator, List<String> labels) {
        LOG.createAppender()
                .appendBold("Select From List By Label:")
                .appendCss(locator)
                .appendProperty("Labels", labels)
                .log();

        HighlightRobotLogger.HtmlAppender appender = LOG.createAppender();
        List<WebElement> options = getSelectListOptions(locator);

        for(int i = 0; i < options.size(); i++) {
            WebElement option = options.get(i);

            if(labels.contains(option.getText())) {
                appender.append(String.format("option[index=%d,value=%s]", i, option.getAttribute("value")), option.getText());
                options.get(i).click();

                if(labels.size() == 1) {
                    // single selection so skip checking
                    // other options
                    break;
                }
            }
        }

        appender.log();
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

    public void mouseDown(String locator) {
        LOG.createAppender()
                .appendBold("Mouse Down:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).clickAndHold(el).release().perform();
    }

    public void mouseDownOnImage(String locator){
        LOG.createAppender()
                .appendBold("Mouse Down On Image:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator, "image");

        new Actions(driver).clickAndHold(el).perform();
    }
    public void mouseDownOnLink(String locator) {
        LOG.createAppender()
                .appendBold("Mouse Down On Link:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator, "link");

        new Actions(driver).clickAndHold(el).perform();
    }

    public void mouseOut(String locator) {
        LOG.createAppender()
                .appendBold("Mouse Out:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        Dimension size = el.getSize();
        int offsetX = (size.getWidth() / 2 ) + 1;
        int offsetY = (size.getHeight() / 2 ) + 1;

        new Actions(driver).moveToElement(el).moveByOffset(offsetX, offsetY).perform();
    }

    public void mouseOver(String locator) {
        LOG.createAppender()
                .appendBold("Mouse Over:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).moveToElement(el).perform();
    }

    public void mouseUp(String locator) {
        LOG.createAppender()
                .appendBold("Mouse Up:")
                .appendCss(locator)
                .log();

        WebElement el = finder.find(locator);

        if (el == null) {
            throw new IllegalStateException(String.format("ERROR: Element %s not found", locator));
        }

        new Actions(driver).clickAndHold(el).release().perform();
    }

    public void pageShouldContain(String text) {
        if (pageContains(text)) {
            LOG.info(String.format("Current page contains text '%s'.", text));
        } else {
            throw new AssertionError(String.format("Page should have contained text %s but did not.", text));
        }
    }

    public void pageShouldContainButton(String locator) {
        if(!isElementPresent(locator, "button")) {
            if(!isElementPresent(locator, "input")) {
                throw new AssertionError(String.format("Page should have contained button '%s' but did not", locator));
            }
        }

        LOG.info(String.format("Current page contains button '%s'.", locator));
    }

    public void pageShouldContainCheckbox(String locator) {
        if(!isElementPresent(locator, "input", "type", "checkbox")) {
            throw new AssertionError(String.format("Page should have contained checkbox '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains checkbox '%s'.", locator));
    }

    public void pageShouldContainElement(String locator) {
        if(!isElementPresent(locator)) {
            throw new AssertionError(String.format("Page should have contained element '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains element '%s'.", locator));
    }

    public void pageShouldContainImage(String locator) {
        if(!isElementPresent(locator,"img")) {
            throw new AssertionError(String.format("Page should have contained image '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains image '%s'.", locator));
    }

    public void pageShouldContainLink(String locator) {
        if(!isElementPresent(locator,"a")) {
            throw new AssertionError(String.format("Page should have contained link '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains link '%s'.", locator));
    }

    public void pageShouldContainList(String locator) {
        if(!isElementPresent(locator,"select")) {
            throw new AssertionError(String.format("Page should have contained list '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains list '%s'.", locator));
    }

    public void pageShouldContainRadio(String locator) {
        if(!isElementPresent(locator, "input", "type", "radio")) {
            throw new AssertionError(String.format("Page should have contained radio '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains radio '%s'.", locator));
    }

    public void pageShouldContainTextfield(String locator) {
        if(!isElementPresent(locator, "input", "type", "text")) {
            throw new AssertionError(String.format("Page should have contained textfield '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains textfield '%s'.", locator));
    }

    public void pageShouldContainPassword(String locator) {
        if(!isElementPresent(locator, "input", "type", "password")) {
            throw new AssertionError(String.format("Page should have contained password field '%s' but did not", locator));
        }

        LOG.info(String.format("Current page contains password field '%s'.", locator));
    }

    public void pageShouldNotContain(String text) {
        if (pageContains(text)) {
            throw new AssertionError(String.format("Page should not have contained text %s but did.", text));
        } else {
            LOG.info(String.format("Current page contains text '%s'.", text));
        }
    }

    public void pageShouldNotContainButton(String locator) {
        if(isElementPresent(locator, "button") || isElementPresent(locator, "input")) {
            throw new AssertionError(String.format("Page should not have contained button '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains button '%s'.", locator));
    }

    public void pageShouldNotContainCheckbox(String locator) {
        if(isElementPresent(locator, "input", "type", "checkbox")) {
            throw new AssertionError(String.format("Page should not have contained checkbox '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains checkbox '%s'.", locator));
    }

    public void pageShouldNotContainElement(String locator) {
        if(isElementPresent(locator)) {
            throw new AssertionError(String.format("Page should not have contained element '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains element '%s'.", locator));
    }

    public void pageShouldNotContainImage(String locator) {
        if(isElementPresent(locator,"img")) {
            throw new AssertionError(String.format("Page should not have contained image '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains image '%s'.", locator));
    }

    public void pageShouldNotContainLink(String locator) {
        if(isElementPresent(locator,"a")) {
            throw new AssertionError(String.format("Page should not have contained link '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains link '%s'.", locator));
    }

    public void pageShouldNotContainList(String locator) {
        if(isElementPresent(locator,"select")) {
            throw new AssertionError(String.format("Page should not have contained list '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains list '%s'.", locator));
    }

    public void pageShouldNotContainRadio(String locator) {
        if(isElementPresent(locator, "input", "type", "radio")) {
            throw new AssertionError(String.format("Page should not have contained radio '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains radio '%s'.", locator));
    }

    public void pageShouldNotContainTextfield(String locator) {
        if(isElementPresent(locator, "input", "type", "text")) {
            throw new AssertionError(String.format("Page should not have contained textfield '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains textfield '%s'.", locator));
    }

    public void pageShouldNotContainPassword(String locator) {
        if(isElementPresent(locator, "input", "type", "password")) {
            throw new AssertionError(String.format("Page should not have contained password field '%s' but did.", locator));
        }

        LOG.info(String.format("Current page contains password field '%s'.", locator));
    }

    public void pressKey(String locator, String key) {
        if (key.startsWith("\\") && key.length() > 1) {
            int keyCode = Integer.valueOf(key.substring(2));
            key = mapAsciiKeyCodeToKey(keyCode) ;
        }

        if (key.length() > 1) {
            throw new IllegalArgumentException(String.format("Key value '%s' is invalid.", key));
        }

        WebElement el = finder.find(locator);
        el.sendKeys(key);
    }

    public void reloadPage() {
        driver.navigate().refresh();
    }

    public void radioButtonShouldBeSetTo(String groupName, String valueSelected) {
        LOG.info(String.format("Verifying radio button '%s' has selection '%s'", groupName, valueSelected));

        List<WebElement> els = getRadioButton(groupName);
        String actualValueSelected = getValueFromRadioButtons(els);

        if (actualValueSelected == null || !actualValueSelected.equalsIgnoreCase(valueSelected)) {
            throw new AssertionError(String.format("Selection of radio button '%s' should have been '%s' but was '%s'", groupName, valueSelected, actualValueSelected));
        }
    }

    public void radioButtonShouldNotBeSelected(String groupName) {
        LOG.info(String.format("Verifying radio button '%s' has no selection", groupName));

        List<WebElement> els = getRadioButton(groupName);
        String actualValue = getValueFromRadioButtons(els);

        if (actualValue != null) {
            throw new AssertionError(String.format("Radio button group '%s' should not have had selection, but '%s' was selected", groupName, actualValue));
        }
    }

    public void selectRadioButton(String groupName, String value) {
        LOG.info(String.format("Selecting '%s' from radio button '%s'", value, groupName));

        WebElement el = getRadioButtonWithValue(groupName, value);
        if (!el.isSelected()) {
            el.click();
        }
    }

    public void selectCheckbox(String locator) {
        LOG.info(String.format("Selecting checkbox '%s'.", locator));

        WebElement el = finder.find(locator, true, "input");
        if (!el.isSelected()) {
            el.click();
        }
    }

    public void unselectCheckbox(String locator) {
        LOG.info("Unselecting checkbox '%s'.", locator);

        WebElement el = getCheckbox(locator);
        if (el.isSelected()) {
            el.click();
        }
    }

    public void selectFrame(String locator) {
        LOG.info(String.format("Selecting frame '%s'.", locator));
        WebElement el = finder.find(locator);
        driver.switchTo().frame(el);
    }

    public void unselectFrame() {
        driver.switchTo().defaultContent();
    }

    public List<String> getWindowHandles() {
        return new ArrayList<String>(driver.getWindowHandles());
    }

    public String getWindowHandle() {
        return driver.getWindowHandle();
    }

    public void selectWindow(String windowName) {
        driver.switchTo().window(windowName);
    }

    public void submitForm(String locator) {
        LOG.info("Submitting form '%s'.", locator);

        WebElement el = finder.find(locator, "form");
        el.submit();
    }

    public void textfieldValueShouldBe(String locator, String expectedValue) {
        WebElement el = finder.find(locator,"input");

        String actual = null;
        if (el == null) {
            el = finder.find(locator,"textarea");
        }

        if (el != null) {
            actual = el.getAttribute("value");
        }

        if (!StringUtils.equalsIgnoreCase(actual, expectedValue)) {
            throw new AssertionError(String.format("Value of text field '%s' should have been '%s' but was '%s'", locator, expectedValue, actual));
        }

        LOG.info(String.format("Content of text field '%s' is '%s'.", locator, expectedValue));
    }

    public void titleShouldBe(String title) {
        if (!driver.getTitle().equalsIgnoreCase(title)) {
            throw new AssertionError(String.format("Title should have been '%s' but was '%s'", title, driver.getTitle()));
        }

        LOG.info(String.format("Page title is '%s'.", title));
    }

    public void delay(long pollMillis) {
        try {
            Thread.sleep(pollMillis);
            LOG.info(String.format("Delayeds for %d millis'.", pollMillis));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    
    public boolean waitForJavaScriptCondition(final String javaScript, int timeOutInSeconds) {
    	
    	boolean jscondition = false;
    	try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	        new WebDriverWait(driver, timeOutInSeconds) {
	        }.until(new ExpectedCondition<Boolean>() {

	            @Override
	            public Boolean apply(WebDriver driverObject) {
	            	return (Boolean) ((JavascriptExecutor) driverObject).executeScript(javaScript);
	            }
	        });
	        jscondition =  (Boolean) ((JavascriptExecutor) driver).executeScript(javaScript); 
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jscondition; 
		} catch (Exception e) {
			String.format("timeout (%d s) reached.",  timeOutInSeconds);
		}
    	return false;
    }
    
    public boolean waitForJQueryProcessing(int timeOutInSeconds){
		boolean jQcondition = false; 
		try{	
			driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait() 
	        new WebDriverWait(driver, timeOutInSeconds) {
	        }.until(new ExpectedCondition<Boolean>() {

	            @Override
	            public Boolean apply(WebDriver driverObject) {
	            	return (Boolean) ((JavascriptExecutor) driverObject).executeScript("return jQuery.active == 0");
	            }
	        });
	        jQcondition = (Boolean) ((JavascriptExecutor) driver).executeScript("return jQuery.active == 0");
			driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
			return jQcondition; 
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return jQcondition; 
    }

    public void waitTillElementContainsRegex(String locator, String regex, long pollMillis, long timeoutMillis) {
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


    public void waitTillElementContainsText(String locator, String text, long pollMillis, long timeoutMillis) {
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

    public void waitTillElementFound(String locator, long pollMillis, long timeoutMillis) {
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

    public void waitTillElementVisible(String locator, long pollMillis, long timeoutMillis) {
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
        int actual = getMatchingCSSCount(cssLocator);
        if(actual != count) {
            throw new AssertionError(String.format("Matching css count for %s expected is %d, but was %d.", cssLocator, count, actual));
        }
    }

    public void xpathShouldMatchXTimes(String xpath, int count) {
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

        if (readOnly.equalsIgnoreCase("readonly") || readOnly.equalsIgnoreCase("true")) {
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
        LOG.info("parts:" + Arrays.asList(parts));
        LOG.info("size " + parts.length);
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
        WebElement el = finder.find(locator);

        return (String) executor.executeScript("return arguments[0].innerHTML;", el);
    }

    public void focus(String locator) {
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
        el.sendKeys(text);
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

        if (multipleValue != null && (multipleValue.equalsIgnoreCase("true") || multipleValue.equalsIgnoreCase("multiple"))) {
            return true;
        }

        return false;
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

    public boolean hasElement(String locator) {
        return finder.find(locator, false) != null;
    }
}
