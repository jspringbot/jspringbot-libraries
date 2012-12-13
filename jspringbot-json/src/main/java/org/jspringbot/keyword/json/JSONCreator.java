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
import org.apache.commons.collections.CollectionUtils;
import org.json.JSONException;
import org.jspringbot.syntax.HighlightRobotLogger;

import java.util.Stack;

public class JSONCreator {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(JSONCreator.class);

    private JSONAppender lastAppender;

    private Stack<JSONAppender> appender = new Stack<JSONAppender>();

    public void startJSONObject() {
        appender.push(new JSONObjectAppender());
        lastAppender = null;
    }

    public void startJSONObject(String name) {
        appender.push(new JSONObjectAppender(name));
    }

    public void startJSONArray() {
        appender.push(new JSONArrayAppender());
        lastAppender = null;
    }

    public void startJSONArray(String name) {
        appender.push(new JSONArrayAppender(name));
    }

    public void addJSONObjectItem(String name, Object value) throws JSONException {
        LOG.createAppender()
                .appendBold("JSON Object Item:")
                .appendCode(name).appendCode(" : ")
                .appendCode(value.toString())
                .log();

        getObjectAppender().append(name, value);
    }

    public void addJSONArrayItem(Object value) throws JSONException {
        getArrayAppender().append(value);
    }

    private JSONObjectAppender getObjectAppender() {
        JSONAppender last = appender.peek();

        if(!JSONObjectAppender.class.isInstance(last)) {
            throw new IllegalStateException("Expected last started was json object");
        }

        return (JSONObjectAppender) last;
    }

    private JSONArrayAppender getArrayAppender() {
        JSONAppender last = appender.peek();

        if(!JSONArrayAppender.class.isInstance(last)) {
            throw new IllegalStateException("Expected last started was json array");
        }

        return (JSONArrayAppender) last;
    }

    public void endJSONArray() throws JSONException {
        JSONAppender ended = appender.pop();

        if(!JSONArrayAppender.class.isInstance(ended)) {
            throw new IllegalStateException("Expected last started was json array");
        }

        JSONArrayAppender endedArray = (JSONArrayAppender) ended;

        if(ended.getName() != null) {
            JSONAppender last = appender.peek();

            if(JSONArrayAppender.class.isInstance(last)) {
                addJSONArrayItem(endedArray.getJsonArray());
            } else {
                addJSONObjectItem(ended.getName(), endedArray.getJsonArray());
            }
        } else {
            if(CollectionUtils.isNotEmpty(appender)) {
                JSONAppender last = appender.peek();

                if(JSONArrayAppender.class.isInstance(last)) {
                    addJSONArrayItem(endedArray.getJsonArray());
                    return;
                }


                throw new IllegalStateException("json creation is not properly ended.");
            }

            lastAppender = endedArray;
        }
    }

    public void endJSONObject() throws JSONException {
        JSONAppender ended = appender.pop();

        if(!JSONObjectAppender.class.isInstance(ended)) {
            throw new IllegalStateException("Expected last started was json array");
        }

        JSONObjectAppender endedObject = (JSONObjectAppender) ended;

        if(ended.getName() != null) {
            JSONAppender last = appender.peek();

            if(JSONArrayAppender.class.isInstance(last)) {
                addJSONArrayItem(endedObject.getJsonObject());
            } else {
                addJSONObjectItem(ended.getName(), endedObject.getJsonObject());
            }
        } else {
            if(CollectionUtils.isNotEmpty(appender)) {
                JSONAppender last = appender.peek();

                if(JSONArrayAppender.class.isInstance(last)) {
                    addJSONArrayItem(endedObject.getJsonObject());
                    return;
                }

                throw new IllegalStateException("json creation is not properly ended.");
            }

            lastAppender = endedObject;
        }
    }


    public String getCreatedJSONString() throws TokenStreamException, RecognitionException {
        if(lastAppender == null) {
            throw new IllegalStateException("No created json.");
        }

        if(CollectionUtils.isNotEmpty(appender)) {
            throw new IllegalStateException("json creation is not properly ended.");
        }

        String createdJSONString = lastAppender.toString();

        LOG.createAppender()
                .appendBold("Created JSON String:")
                .appendJSON(JSONHelper.prettyPrint(createdJSONString))
                .log();

        return createdJSONString;
    }

}
