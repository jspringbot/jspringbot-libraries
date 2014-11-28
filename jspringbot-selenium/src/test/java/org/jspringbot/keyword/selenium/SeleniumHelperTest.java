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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class SeleniumHelperTest {

    @Autowired
    public SeleniumHelper helper;

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