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

import junitx.framework.Assert;
import org.jspringbot.keyword.selenium.web.*;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-selenium-chrome.xml"})
public class SeleniumHelperTest {

    @Autowired
    public SeleniumHelper helper;

    @Autowired
    private NavigateTo navigateTo;

    @Autowired
    private SendKeys sendKeys;

    @Autowired
    private ActionStart actionStart;

    @Autowired
    private ActionMoveToElement actionMoveToElement;

    @Autowired
    private ActionClick actionClick;

    @Autowired
    private ActionPerform actionPerform;

    @Autowired
    private ActionKeyUp actionKeyUp;

    @Autowired
    private ActionKeyDown actionKeyDown;

    @Autowired
    private ActionPause actionPause;

    @Autowired
    private ActionSendKeys actionSendKeys;

    @Autowired
    private ActionWaitUntilElementVisible actionWaitUntilElementVisible;

    @Autowired
    private ActionDragAndDrop actionDragAndDrop;

    @Autowired
    private ActionDragAndDropBy actionDragAndDropBy;

    @Autowired
    private ActionClickAndHold actionClickAndHold;

    @Autowired
    private ActionRelease actionRelease;

    @Autowired
    private SimulateDragAndDrop simulateDragAndDrop;

    @Autowired
    private ActionSimulateDragAndDrop actionSimulateDragAndDrop;

    @Autowired
    private ExecuteJavascript executeJavascript;

    @Autowired
    private ElementExecuteJavascript elementExecuteJavascript;

    @Autowired
    private ActionContextClick actionContextClick;


    @Test
    public void keyUpKeyDown() throws Exception {
        doKeyword(navigateTo, "http://jspringbot.org/search.html");
        doKeyword(sendKeys, "id=artist_search", "alvin de leon");

        doKeyword(actionStart);
        doKeyword(actionMoveToElement, "id=artist_search");
        doKeyword(actionClick);
        doKeyword(actionKeyDown, "SHIFT");
        doKeyword(actionSendKeys, "cord=ARROW_LEFT|ARROW_LEFT|ARROW_LEFT|ARROW_LEFT|ARROW_LEFT|ARROW_LEFT|ARROW_LEFT");
        doKeyword(actionKeyUp, "SHIFT");
        doKeyword(actionSendKeys, "hello");
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void keyCord() throws Exception {
        doKeyword(navigateTo, "http://jspringbot.org/search.html");

        doKeyword(actionStart);
        doKeyword(actionWaitUntilElementVisible, "id=artist_search");
        doKeyword(actionClick, "id=artist_search");
        doKeyword(actionSendKeys, "Log");
        doKeyword(actionPause, "1000");
        doKeyword(actionSendKeys, "cord=ARROW_DOWN|ARROW_DOWN|ARROW_DOWN|ARROW_DOWN");
        doKeyword(actionPause, "300");
        doKeyword(actionSendKeys, "cord=ENTER");
        doKeyword(actionSendKeys, "cord=SHIFT|ARROW_DOWN|ARROW_DOWN|ARROW_DOWN|ARROW_DOWN");
        doKeyword(actionContextClick);
        doKeyword(actionSendKeys, "cord=ARROW_DOWN|ARROW_DOWN|ARROW_DOWN");
        doKeyword(actionSendKeys, "cord=ENTER");
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void menuNavigation() throws Exception {
        doKeyword(navigateTo, "http://jspringbot.org");

        doKeyword(actionStart);
        doKeyword(actionMoveToElement, "css=#documentation-menu > a");
        doKeyword(actionClick);
        doKeyword(actionPause, "500");
        doKeyword(actionMoveToElement, "css=#robot-docs-menu-items > li:nth-child(1) > a");
        doKeyword(actionPause, "500");
        doKeyword(actionMoveToElement, "css=#robot-docs-menu-items > li:nth-child(1) > ul > li:nth-child(2) > a");
        doKeyword(actionPause, "500");
        doKeyword(actionClick);
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void actionClickHoldMoveRelease2() throws Exception {
        doKeyword(navigateTo, "http://marcojakob.github.io/dart-dnd/basic/web/");

        doKeyword(actionStart);
        doKeyword(actionMoveToElement, "css=body > div > img:nth-child(3)");
        doKeyword(actionClickAndHold);
        doKeyword(actionPause, "1000");
        doKeyword(actionMoveToElement, "css=.trash", 10, 10);
        doKeyword(actionPause, "1000");
        doKeyword(actionRelease);
        doKeyword(actionPause, "1000");
        doKeyword(actionClickAndHold, "css=body > div > img:nth-child(4)");
        doKeyword(actionPause, "1000");
        doKeyword(actionMoveToElement, "css=.trash", 10, 10);
        doKeyword(actionPause, "1000");
        doKeyword(actionRelease);
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void actionClickHoldMoveRelease() throws Exception {
        doKeyword(navigateTo, "http://marcojakob.github.io/dart-dnd/basic/web/");

        doKeyword(actionStart);
        doKeyword(actionClickAndHold, "css=body > div > img:nth-child(3)");
        doKeyword(actionMoveToElement, "css=.trash");
        doKeyword(actionRelease);
        doKeyword(actionPause, "500");
        doKeyword(actionClickAndHold, "css=body > div > img:nth-child(4)");
        doKeyword(actionMoveToElement, "css=.trash");
        doKeyword(actionRelease);
        doKeyword(actionPause, "500");
        doKeyword(actionClickAndHold, "css=body > div > img:nth-child(5)");
        doKeyword(actionMoveToElement, "css=.trash");
        doKeyword(actionRelease);
        doKeyword(actionPause, "500");
        doKeyword(actionClickAndHold, "css=body > div > img:nth-child(6)");
        doKeyword(actionMoveToElement, "css=.trash");
        doKeyword(actionRelease);
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void actionDragAndDrop() throws Exception {
        doKeyword(navigateTo, "http://marcojakob.github.io/dart-dnd/basic/web/");

        doKeyword(actionStart);
        doKeyword(actionDragAndDrop, "css=body > div > img:nth-child(3)", "css=.trash");
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDrop, "css=body > div > img:nth-child(4)", "css=.trash");
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDrop, "css=body > div > img:nth-child(5)", "css=.trash");
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDrop, "css=body > div > img:nth-child(6)", "css=.trash");
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void actionDragAndDropBy() throws Exception {
        doKeyword(navigateTo, "http://marcojakob.github.io/dart-dnd/basic/web/");

        doKeyword(actionStart);
        doKeyword(actionDragAndDropBy, "css=body > div > img:nth-child(3)", 400, 50);
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDropBy, "css=body > div > img:nth-child(4)", 400, 50);
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDropBy, "css=body > div > img:nth-child(5)", 400, 50);
        doKeyword(actionPause, "500");
        doKeyword(actionDragAndDropBy, "css=body > div > img:nth-child(6)", 400, 50);
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void actionSimulateDragAndDrop() throws Exception {
        doKeyword(navigateTo, "http://www.w3schools.com/html/html5_draganddrop.asp");

        doKeyword(actionStart);
        doKeyword(actionSimulateDragAndDrop, "css=#drag1", "css=#div2");
        doKeyword(actionPause, "1000");
        doKeyword(actionSimulateDragAndDrop, "css=#drag1", "css=#div1");
        doKeyword(actionPerform);

        helper.delay(5000);
    }

    @Test
    public void dragAndDropHtml5() throws Exception {
        doKeyword(navigateTo, "http://www.w3schools.com/html/html5_draganddrop.asp");

        helper.pageShouldContainElement("css=#div1 > img");
        doKeyword(simulateDragAndDrop, "css=#drag1", "css=#div2");
        helper.pageShouldContainElement("css=#div2 > img");
        helper.delay(1000);
        doKeyword(simulateDragAndDrop, "css=#drag1", "css=#div1");
        helper.pageShouldContainElement("css=#div1 > img");

        helper.delay(5000);
    }

    @Test
    public void executeJavascript() throws Exception {
        doKeyword(navigateTo, "http://www.w3schools.com/html/html5_draganddrop.asp");
        doKeyword(executeJavascript, "alert(arguments[0] + arguments[1] + arguments[2]);", "alvin", "-", "test");

        helper.delay(5000);
    }

    @Test
    public void executeElementJavascript() throws Exception {
        doKeyword(navigateTo, "http://www.w3schools.com/html/html5_draganddrop.asp");
        doKeyword(elementExecuteJavascript, "css=body", "arguments[0].innerHTML = '<b>alvin</b>'; alert(arguments[1] + arguments[2] + arguments[3]);", "alvin", "-", "test");

        helper.delay(5000);
    }

    private Object doKeyword(AbstractSeleniumKeyword keyword, Object... objs) throws Exception {
        return keyword.execute(objs);
    }

    @Test
    @Ignore
    public void multipleDomainNavigation() throws IOException {
        helper.navigateTo("http://www.google.com");
        helper.navigateTo("http://yahoo.com");
        helper.navigateTo("http://www.google.com.ph");
        helper.navigateTo("http://github.com");
        helper.navigateTo("http://yahoo.com");
        helper.navigateTo("http://www.google.com");
        helper.navigateTo("http://github.com");
    }

    @Test
    @Ignore
    public void captureScreenshot() throws IOException {
        helper.navigateTo("http://www.google.com");
        helper.captureScreenShot();
    }

    @Test
    @Ignore
    public void testClickLink() throws Exception {
        helper.navigateTo("http://www.google.com");
        helper.clickLink("css=#_eEe a");
    }

    @Test
    @Ignore
    public void testCurrentFrameShouldContain() throws Exception {
        helper.navigateTo("http://www.google.com");
        helper.clickLink("css=#_eEe a");
        helper.currentFrameShouldContain("Sinusuwerte Ako");
    }

    @Test
    @Ignore
    public void testDeleteCookies() {
        helper.navigateTo("http://www.google.com.ph");
        helper.deleteAllCookies();
        try{
            helper.getCookieValue("NID");
            Assert.fail("Should have failed when getting cookie value for cookiename : NID");
        } catch (Exception e) {
        }

        try{
            helper.getCookieValue("PREF");
            Assert.fail("Should have failed when getting cookie value for cookiename : NID");
        } catch (Exception e) {
        }
    }

    @Test
    @Ignore
    public void testDeleteCookie() {
        helper.navigateTo("http://www.google.com.ph");
        helper.deleteCookie("NID");
        try{
            helper.getCookieValue("NID");
            Assert.fail("Should have failed when getting cookie value for cookiename : NID");
        } catch (Exception e) {
        }
    }

    @Test
    @Ignore
    public void testElementShouldContain() {
        helper.navigateTo("http://www.google.com");
        helper.elementShouldContain("css=#_eEe", "Google.com.ph offered in:");
    }

    @Test
    @Ignore
    // TODO - Still failing
    public void testGetAllLinks() throws Exception {
        helper.navigateTo("http://www.google.com.ph");
        System.out.println(helper.getAllLinks());
    }

    @Test
    @Ignore
    public void testGetCookieValue() throws Exception {
        helper.navigateTo("http://www.google.com.ph");
        System.out.println(helper.getCookieValue("NID"));
        System.out.println(helper.getCookieValue("PREF"));

        try{
            System.out.println(helper.getCookieValue("NON_EXISTENT_COOKIE"));
            Assert.fail("Should have failed when getting cookie value for cookiename : SHIELA");
        } catch (Exception e) {
        }
    }

    @Test
    @Ignore
    public void testGetTitle() {
        helper.navigateTo("http://www.google.com.ph");
        Assert.assertEquals("Google", helper.getTitle());
    }

    @Test
    @Ignore
    public void testInputPassword() {
        helper.navigateTo("http://login.yahoo.com");
        helper.waitTillElementFound("id=passwd", 500, 10000);
        helper.inputPassword("id=passwd", "password");
    }

    @Test
    @Ignore
    public void testOpenBrowser() {
        helper.navigateTo("http://www.google.com");
    }

    @Test
    @Ignore
    public void testPageShouldContain() {
        helper.navigateTo("http://login.yahoo.com");
        helper.pageShouldContain("Sign in to Yahoo");
    }

    @Test
    @Ignore
    public void testPageShouldContainButton() {
        helper.navigateTo("http://www.google.com.ph/");
        helper.pageShouldContainButton("id=gbqfba");
    }

    @Test
    @Ignore
    public void testPageShouldContainCheckbox() {
        helper.navigateTo("http://www.echoecho.com/htmlforms09.htm");
        helper.pageShouldContainCheckbox("name=option1");
    }
}