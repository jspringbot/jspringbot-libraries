/*
 * Copyright (c) 2012. JSpringBot Shiela D. Buitizon. All Rights Reserved.
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

package org.jspringbot.keyword.json;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.script.ScriptException;
import java.util.Arrays;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:sample-json.xml"})
public class JSONCreatorTest {

    @Autowired
    private JSONHelper helper;

    @Autowired
    private JSONCreator creator;

    @Autowired
    private StartJSONObject startJSONObject;

    @Test
    public void testGetJsonValues() throws JSONException, TokenStreamException, RecognitionException {
        creator.startJSONObject();
        creator.startJSONObject("store");
        creator.startJSONArray("book");

        creator.startJSONObject();
        creator.addJSONObjectItem("category","reference");
        creator.addJSONObjectItem("author","Nigel Rees");
        creator.addJSONObjectItem("title","Sayings of the Century");
        creator.addJSONObjectItem("price",8.95);
        creator.endJSONObject();

        creator.startJSONObject();
        creator.addJSONObjectItem("category","fiction");
        creator.addJSONObjectItem("author","Evelyn Waugh");
        creator.addJSONObjectItem("title","Sword of Honour");
        creator.addJSONObjectItem("price",12.99);
        creator.addJSONObjectItem("isbn","0-553-21311-3");
        creator.endJSONObject();

        creator.endJSONArray();

        creator.startJSONObject("bicycle");
        creator.addJSONObjectItem("model", "bmx");
        creator.addJSONObjectItem("color", "red");
        creator.addJSONObjectItem("price",19.95);
        creator.endJSONObject();
        creator.endJSONObject();
        creator.endJSONObject();

        String jsonString = creator.getCreatedJSONString();
        System.out.println(JSONHelper.prettyPrint(jsonString));

        helper.setJsonString(jsonString);
        assertEquals("Should be equal", helper.getJsonValue("store.bicycle.color"), "red");
        assertTrue("Should be equal", helper.getJsonValues("$..category").containsAll(Arrays.asList("reference","fiction")));

        try{
            helper.jsonValueShouldBe("store.bicycle.price","19.95");
        } catch (Exception e) {
            fail("Should not throw exception");
        }

        try {
            helper.jsonValueShouldBeNull("store.book[1].author");
            fail("Should throw an exception");
        }  catch (Exception e) { }

        try {
            helper.jsonValueShouldNotBeNull("store.book[1].author");
        }  catch (Exception e) {
            fail("Should not throw an exception");
        }
    }

    @Test
    public void testStartJSONObject() throws Exception {
        creator.startJSONObject();
        creator.addJSONObjectItem("booleanValue", true);
        creator.addJSONObjectItem("stringValue", "hello");
        creator.addJSONObjectItem("numberValue", 123);
        creator.startJSONArray("list");
        creator.addJSONArrayItem(1);
        creator.addJSONArrayItem(2);
        creator.addJSONArrayItem(3);
        creator.endJSONArray();
        creator.startJSONObject("jsonObject");
        creator.addJSONObjectItem("objname", "objvalue");
        creator.endJSONObject();
        creator.endJSONObject();
        System.out.println(creator.getCreatedJSONString());
    }

    @Test
    public void testStartJSONArray() throws Exception {
        creator.startJSONObject();
        creator.startJSONArray("outerList");
        creator.addJSONArrayItem(true);
        creator.addJSONArrayItem("hello");
        creator.addJSONArrayItem(123);
        creator.startJSONArray("innerList");
        creator.addJSONArrayItem("a");
        creator.addJSONArrayItem("b");
        creator.addJSONArrayItem("c");
        creator.endJSONArray();
        creator.endJSONArray();
        creator.endJSONObject();
        System.out.println(creator.getCreatedJSONString());
    }

    @Test
    public void testStartJSONArrayOnly() throws Exception {
        creator.startJSONArray();
        creator.addJSONArrayItem(true);
        creator.addJSONArrayItem("hello");
        creator.addJSONArrayItem(123);
        creator.startJSONArray();
        creator.addJSONArrayItem("a");
        creator.addJSONArrayItem("b");
        creator.addJSONArrayItem("c");
        creator.endJSONArray();
        creator.endJSONArray();
        System.out.println(creator.getCreatedJSONString());
    }

    @Test
    public void testJsonArrayLengthShouldBe() throws JSONException, TokenStreamException, RecognitionException, ScriptException {
        creator.startJSONObject();
        creator.startJSONObject("rootObject");
        creator.startJSONArray("emptyList");
        creator.endJSONArray();
        creator.startJSONArray("nonEmptyList");
        creator.addJSONArrayItem("hello");
        creator.addJSONArrayItem("world");
        creator.endJSONArray();
        creator.endJSONObject();
        creator.endJSONObject();
        helper.setJsonString(creator.getCreatedJSONString());
        helper.jsonArrayLengthShouldBe("rootObject.emptyList", 0);
        helper.jsonArrayLengthShouldBe("rootObject.nonEmptyList", 2);
    }

    @Test
    public void testStartJSONObject2() throws Exception {
        startJSONObject.execute(new Object[]{});
    }
}
