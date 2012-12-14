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

package org.jspringbot.keyword.json;

import antlr.RecognitionException;
import antlr.TokenStreamException;
import com.jayway.jsonpath.JsonPath;
import com.sdicons.json.model.JSONValue;
import com.sdicons.json.parser.JSONParser;
import org.jspringbot.syntax.HighlightRobotLogger;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

public class JSONHelper {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(JSONHelper.class);

    protected  String jsonString;

    protected ScriptEngine engine;

    public void setJsonString(String jsonString) {
        try {
            LOG.createAppender()
                    .appendBold("JSON String:")
                    .appendJSON(prettyPrint(jsonString))
                    .log();
        } catch (Exception e) {
            throw new IllegalStateException(e.getMessage(), e);
        }

        this.jsonString = jsonString;
    }

    public static String prettyPrint(String jsonString) throws TokenStreamException, RecognitionException {
        final JSONParser lParser = new JSONParser(new StringReader(jsonString));
        final JSONValue lMyObject = lParser.nextValue();

        return lMyObject.render(true);
    }

    public String getJsonString() {
        return jsonString;
    }

    @SuppressWarnings("unchecked")
    public List<Object> getJsonValues(String jsonExpression) {
        Object jsonValue = JsonPath.read(jsonString, "$." + jsonExpression);

        List items;

        if(jsonValue instanceof List) {
            items = (List) jsonValue;
        } else {
            items = Arrays.asList(jsonValue);
        }

        LOG.createAppender()
                .appendBold("Get JSON Values:")
                .appendProperty("Json Expression", jsonExpression)
                .appendProperty("Size", items.size())
                .log();

        return items;
    }

    public Object getJsonValue(String jsonExpression) {
        Object jsonValue = JsonPath.read(jsonString, "$." + jsonExpression);

        if(jsonValue instanceof List) {
            jsonValue = ((List) jsonValue).iterator().next();
        }

        LOG.createAppender()
                .appendBold("Get JSON Value:")
                .appendProperty("Json Expression", jsonExpression)
                .appendProperty("Json Value", String.valueOf(jsonValue))
                .log();

        return jsonValue;
    }

    public void jsonValueShouldBe(String jsonExpression, String expectedValue) {
        Object jsonValue = getJsonValue(jsonExpression);

        if (Number.class.isInstance(jsonValue)) {
            Double expectedNumberValue = Double.valueOf(expectedValue);
            if (!expectedNumberValue.equals(jsonValue)) {
                throw new IllegalArgumentException(String.format("Expecting '%s' json value but was '%s'",expectedValue,String.valueOf(jsonValue)));
            }
        } else {
            if (!expectedValue.equals(String.valueOf(jsonValue))) {
                throw new IllegalArgumentException(String.format("Expecting '%s' json value but was '%s'",expectedValue,String.valueOf(jsonValue)));
            }
        }
    }

    public void jsonValueShouldBeNull(String jsonExpression) {
        Object jsonValue = getJsonValue(jsonExpression);

        if (jsonValue != null) {
            throw new IllegalArgumentException(String.format("Expecting null json value but was '%s'",String.valueOf(jsonValue)));
        }
    }

    public void jsonValueShouldNotBeNull(String jsonExpression) {
        Object jsonValue = getJsonValue(jsonExpression);

        if (jsonValue == null) {
            throw new IllegalArgumentException("Expecting non null json value but was null");
        }
    }

    public int getJsonArrayLength(String jsonExpression) {
        try {
            ScriptEngineManager manager = new ScriptEngineManager();
            engine = manager.getEngineByName("JavaScript");
            engine.eval("var json = " + jsonString + ";");
            engine.eval("var jsonExpr = json." + jsonExpression + ".length;");

            int length = ((Number) engine.get("jsonExpr")).intValue();

            LOG.createAppender()
                    .appendBold("Get JSON Array Length:")
                    .appendProperty("Json Expression", jsonExpression)
                    .appendProperty("Length", length)
                    .log();

            return length;
        } catch(Exception e) {
            throw new IllegalStateException("Response is not json.", e);
        }
    }

    public void jsonArrayLengthShouldBe(String jsonExpression, int expectedLength) {
        int length = getJsonArrayLength(jsonExpression);
        if (length != expectedLength) {
            throw new IllegalStateException(String.format("Json Array length should be %s but was %s", expectedLength, length));
        }
    }
}
