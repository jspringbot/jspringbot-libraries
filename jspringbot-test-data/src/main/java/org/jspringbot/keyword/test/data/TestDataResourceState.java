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

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.*;

/**
 * Resource state holder
 */
public class TestDataResourceState {
    private Cache cache;

    private String name;

    private String baseKey;

    private List<String> headerLine;

    private Map<String, Integer> headers;

    private int totalSize;

    private Resource resource;

    public TestDataResourceState(String name, Cache cache, Resource resource) {
        this.name = name;
        this.cache = cache;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public int getContentSize(int index) {
        return getTestData(index).getSize();
    }

    public void parse() throws IOException {
        InputStreamReader isr = null;
        headers = null;
        headerLine = null;
        totalSize = 0;
        int currentIndex = 0;
        baseKey = name + "/";

        try {
            isr = new InputStreamReader(resource.getInputStream());
            CSVReader reader = new CSVReader(isr);

            String[] nextLine;
            TestData currentTestData = null;
            while((nextLine = reader.readNext()) != null) {
                if(nextLine.length == 0) {
                    continue;
                }

                // retrieve csv header
                if(headers == null) {
                    headerLine = new LinkedList<String>();
                    headers = new HashMap<String, Integer>(nextLine.length);
                    for(int i = 0; i < nextLine.length; i++) {
                        headers.put(nextLine[i].toLowerCase(), i);
                        headerLine.add(nextLine[i].toUpperCase());
                    }
                    continue;
                }

                if(StringUtils.isNotBlank(nextLine[0])) {
                    if(currentTestData != null) {
                        cache.put(new Element(buildKey(currentIndex++), currentTestData));
                    }

                    currentTestData = new TestData(Arrays.asList(nextLine));
                } else if(currentTestData != null) {
                    currentTestData.addContent(Arrays.asList((String[]) ArrayUtils.subarray(nextLine, 1, nextLine.length)));
                }
            }

            if(currentTestData != null) {
                cache.put(new Element(buildKey(currentIndex), currentTestData));
            }

            totalSize = currentIndex + 1;
        } finally {
            IOUtils.closeQuietly(isr);
        }
    }

    private String buildKey(int index) {
        return baseKey + index;
    }

    public String getHeaderByIndex(int index, int columnIndex) {
        return getTestData(index).getHeader(columnIndex);
    }

    public String getHeaderByName(int index, String columnName) {
        if(!headers.containsKey(columnName.toLowerCase())) {
            throw new IllegalArgumentException(String.format("Header with '%s' not found!", columnName));
        }

        return getHeaderByIndex(index, headers.get(columnName.toLowerCase()));
    }

    public String getContentByIndex(int index, int contentIndex, int contentColumnIndex) {
        TestData data = getTestData(index);
        return data.getContent(contentIndex, contentColumnIndex);
    }

    public List<String> getContentAsListByName(int index, String keyColumnName, String key, String valueColumnName) {
        if(!headers.containsKey(keyColumnName.toLowerCase())) {
            throw new IllegalArgumentException(String.format("Header with '%s' not found!", keyColumnName));
        }
        if(!headers.containsKey(valueColumnName.toLowerCase())) {
            throw new IllegalArgumentException(String.format("Header with '%s' not found!", valueColumnName));
        }

        TestData data = getTestData(index);
        return data.getContentAsList(headers.get(keyColumnName.toLowerCase()) - data.getHeaderColumnSize(), key, headers.get(valueColumnName.toLowerCase()) - data.getHeaderColumnSize());
    }

    public String getContentByName(int index, int contentIndex, String contentColumnName) {
        if(!headers.containsKey(contentColumnName.toLowerCase())) {
            throw new IllegalArgumentException(String.format("Header with '%s' not found!", contentColumnName));
        }

        TestData data = getTestData(index);
        return data.getContent(contentIndex, headers.get(contentColumnName.toLowerCase()) - data.getHeaderColumnSize());
    }

    public List<Integer> getContentKeys(int index) {
        return getTestData(index).buildContentIndices();
    }

    public String getCSVContent(int index) throws IOException {
        return getTestData(index).getCSVContent();
    }

    public String getCSVContentHeader(int index) {
        TestData data = getTestData(index);

        List<String> contentHeader = new LinkedList<String>();
        for(int i = data.getHeaderColumnSize(); i < headerLine.size(); i++) {
            contentHeader.add(headerLine.get(i));
        }

        StringWriter str = new StringWriter();
        CSVWriter writer = new CSVWriter(str);

        try {
            writer.writeNext(contentHeader.toArray(new String[contentHeader.size()]));
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(str);
        }

        return str.toString();
    }

    private TestData getTestData(int index) {
        Element element = cache.get(buildKey(index));
        return (TestData) element.getValue();
    }
}
