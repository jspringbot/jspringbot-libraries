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

package org.jspringbot.keyword.test.data;

import au.com.bytecode.opencsv.CSVWriter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import org.jspringbot.JSpringBotLogger;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Creates a test data helper.
 */
public class TestDataHelper {

    public static final JSpringBotLogger LOG = JSpringBotLogger.getLogger(TestDataHelper.class);

    private Cache cache;

    private CSVWriter writer;

    private TestDataResourceState currentState;

    private Map<String, TestDataResourceState> stateMap;

    public TestDataHelper(CacheManager manager) {
        this.cache = manager.getCache(TestData.class.getName());
        this.stateMap = new HashMap<String, TestDataResourceState>();
    }

    public void createTestData(File path, List<String> headers) throws IOException {
        writer = new CSVWriter(new FileWriter(path));
        writer.writeNext(headers.toArray(new String[headers.size()]));
        writer.flush();
    }

    public void addTestData(List<String> headers) throws IOException {
        writer.writeNext(headers.toArray(new String[headers.size()]));
        writer.flush();
    }

    public void doneCreation() throws IOException {
        writer.close();
    }

    public void switchTestData(String name) {
        if(!stateMap.containsKey(name)) {
            throw new IllegalArgumentException(String.format("No state with name '%s'", name));
        }

        LOG.html("Switching to <b>%s</b>", name);
        currentState = stateMap.get(name);
    }

    public void parseStateResource(String name, Resource resource) throws IOException {
        currentState = new TestDataResourceState(name, cache, resource);
        currentState.parse();
        stateMap.put(name, currentState);
    }

    public void parseResource(Resource resource) throws IOException {
        String name = resource.getFilename();
        parseStateResource(name, resource);
    }

    public List<Integer> getKeys() {
        List<Integer> indices = new ArrayList<Integer>();

        for(int i = 0; i < currentState.getTotalSize(); i++) {
            indices.add(i);
        }

        return indices;
    }

    public String getHeaderByIndex(int index, int columnIndex) {
        return currentState.getHeaderByIndex(index, columnIndex);
    }

    public String getHeaderByName(int index, String columnName) {
        return currentState.getHeaderByName(index, columnName);
    }

    public String getContentByIndex(int index, int contentIndex, int contentColumnIndex) {
        return currentState.getContentByIndex(index, contentIndex, contentColumnIndex);
    }

    public List<String> getContentAsListByName(int index, String keyColumnName, String key, String valueColumnName) {
        return currentState.getContentAsListByName(index, keyColumnName, key, valueColumnName);
    }

    public String getContentByName(int index, int contentIndex, String contentColumnName) {
        return currentState.getContentByName(index, contentIndex, contentColumnName);
    }

    public List<Integer> getContentKeys(int index) {
        return currentState.getContentKeys(index);
    }

    public int getSize() {
        return currentState.getTotalSize();
    }

    public String getCSVContent(int index) throws IOException {
        return currentState.getCSVContentHeader(index) + currentState.getCSVContent(index);
    }

    public String getCSVContentHeader(int index) throws IOException {
        return currentState.getCSVContentHeader(index);
    }

    public int getContentSize(int index) {
        return currentState.getContentSize(index);
    }
}
