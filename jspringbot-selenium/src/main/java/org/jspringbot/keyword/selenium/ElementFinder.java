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
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * PURPOSELY DID NOT ADD MAPPING FOR TAG:

 if tag == 'link':
 tag = 'a'
 elif tag == 'image':
 tag = 'img'
 elif tag == 'list':
 tag = 'select'
 elif tag == 'radio button':
 tag = 'input'
 constraints['type'] = 'radio'
 elif tag == 'checkbox':
 tag = 'input'
 constraints['type'] = 'checkbox'
 elif tag == 'text field':
 tag = 'input'
 constraints['type'] = 'text'
 elif tag == 'file upload':
 tag = 'input'
 constraints['type'] = 'file'
 */
public class ElementFinder {

    protected WebDriver driver;

    public ElementFinder(WebDriver driver) {
        this.driver = driver;
    }

    public WebElement find(String locator) {
        return find(locator, true, null, null);
    }

    public WebElement find(String locator, boolean validateResult) {
        return find(locator, validateResult, null, null);
    }

    public WebElement find(String locatorStr, String tagName) {
        return find(locatorStr, true, tagName, null);
    }

    public WebElement find(String locatorStr, boolean validateResult, String tagName) {
        return find(locatorStr, validateResult, tagName, null);
    }

    public WebElement find(String locatorStr, boolean validateResult, String tagName, String attrName, String attrValue) {
        Map<String, String> attrs = new HashMap<String, String>();
        attrs.put(attrName, attrValue);

        return find(locatorStr, validateResult, tagName, attrs);
    }

    public WebElement find(String locatorStr, boolean validateResult, String tagName, Map<String,String> attributes) {
        Locator locator = parseLocator(locatorStr);

        WebElement element;

        if(locator.prefix != null) {
            element = LocatorEnum.findByPrefix(locator.prefix).find(driver, locator.criteria, tagName, attributes);
        } else {
            element = LocatorEnum.XPATH.find(driver, locator.criteria, tagName, attributes);
        }

        if (validateResult) {
            //Validate.notNull(element, String.format("No element found given locator '%s'.", locatorStr));
        	/*
        	output.xml file being corrupted when i18n is being included as locator. i.e. IllegalStateException: No element found given locator 'text=登出'
            UnicodeEncodeError: 'ascii' codec can't encode characters in position 60-61: ordinal not in range(128)
        	temporarily removing locatorStr.
        	*/
        	Validate.notNull(element, "Element locator not found.");
        }

        return element;
    }

    private Locator parseLocator(String locatorStr) {
        Locator locator = new Locator();

        locator.prefix = null;
        locator.criteria = locatorStr;

        if (!StringUtils.startsWith(locatorStr, "//")) {
            int equalIndexOf =locatorStr.indexOf('=');

            if(equalIndexOf != -1) {
                locator.prefix = locatorStr.substring(0, equalIndexOf);
                locator.criteria = locatorStr.substring(equalIndexOf + 1);
            }
        }

        return locator;
    }

    public static WebElement findById(WebDriver driver, String identifier, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.id(identifier)), tagName, attributes);
    }

    public static WebElement findByName(WebDriver driver, String name, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.name(name)), tagName, attributes);
    }

    public static WebElement findByXpath(WebDriver driver, String xpathExpression, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.xpath(xpathExpression)), tagName, attributes);
    }

    public static WebElement findByTag(WebDriver driver, String tag, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.tagName(tag)), tagName, attributes);
    }

    @SuppressWarnings("unchecked")
    public static WebElement findByDom(WebDriver driver, String locator, String tagName, Map<String,String> attributes) {
        JavascriptExecutor executor = (JavascriptExecutor) driver;
        Object result = executor.executeScript(String.format("return %s;", locator));

        if (result != null) {
            if(List.class.isInstance(result)) {
                return filterElements((List<WebElement>) result, tagName, attributes);
            } else {
                return filterElements(Collections.singletonList((WebElement) result), tagName, attributes);
            }
        }

        return null;
    }

    public static WebElement findByLinkText(WebDriver driver, String linkText, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.linkText(linkText)), tagName, attributes);
    }

    public static WebElement findByPartialLinkText(WebDriver driver, String linkText, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.partialLinkText(linkText)), tagName, attributes);
    }

    public static WebElement findByCSS(WebDriver driver, String cssSelector, String tagName, Map<String,String> attributes) {
        return filterElements(driver.findElements(By.cssSelector(cssSelector)), tagName, attributes);
    }


    private static WebElement filterElements(List<WebElement> elements, String tagName, Map<String,String> attributes) {
        if(CollectionUtils.isEmpty(elements)) {
            return null;
        }

        if(StringUtils.isEmpty(tagName)) {
            return elements.iterator().next();
        }

        for(WebElement element : elements) {
            if(element.getTagName().equalsIgnoreCase(tagName)) {
                if(MapUtils.isEmpty(attributes)) {
                    return element;
                } else {
                    for(Map.Entry<String, String> entry : attributes.entrySet()) {
                        String value = element.getAttribute(entry.getKey());

                        if(StringUtils.equalsIgnoreCase(entry.getValue(), value)) {
                            return element;
                        }
                    }
                }
            }
        }

        return null;
    }

    public boolean isLocator(String locator) {
        for(LocatorEnum locatorEnum : LocatorEnum.values()) {
            if(StringUtils.startsWith(locator, locatorEnum.prefix + "=")) {
                return true;
            }
        }

        return false;
    }

    private class Locator {
        public String prefix;
        public String criteria;
    }

    private enum LocatorEnum {
        ID("id") {
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findById(driver, locator, tagName, attributes);
            }
        },

        NAME("name") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByName(driver, locator, tagName, attributes);
            }
        },

        XPATH("xpath") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByXpath(driver, locator, tagName, attributes);
            }
        },

        DOM("dom") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByDom(driver, locator, tagName,attributes);
            }
        },

        LINK("link") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByLinkText(driver, locator, tagName, attributes);
            }
        },

        CSS("css") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByCSS(driver, locator, tagName, attributes);
            }
        },

        TAG("tag") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByTag(driver, locator, tagName, attributes);
            }
        },

        TEXT("text") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByLinkText(driver, locator, tagName, attributes);
            }
        },

        PARTIAL_TEXT("partial") {
            @Override
            public WebElement find(WebDriver driver, String locator, String tagName, Map<String, String> attributes) {
                return findByPartialLinkText(driver, locator, tagName, attributes);
            }
        };

        private String prefix;

        private LocatorEnum(String prefix) {
            this.prefix = prefix;
        }

        public static LocatorEnum findByPrefix(String prefix) {
            for(LocatorEnum locator : values()) {
                if(locator.prefix.equals(prefix)) {
                    return locator;
                }
            }

            throw new IllegalArgumentException(String.format("Locator prefix '%s' not supported.", prefix));
        }

        public abstract WebElement find(WebDriver driver, String locator, String tagName, Map<String,String> attributes);
    }

}
