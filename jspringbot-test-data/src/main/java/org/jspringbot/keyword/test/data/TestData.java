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
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single test data
 */
public class TestData implements Serializable {
    private List<String> headers;

    private List<List<String>> contents;

    private int headerColumnSize;

    public TestData(List<String> headers) {
        this.headers = headers;

        headerColumnSize = 0;
        for(String header : headers) {
            if(StringUtils.isNotEmpty(header)) {
                headerColumnSize++;
            }
        }
    }

    public void addContent(List<String> content) {
        if(CollectionUtils.isEmpty(content)) {
            return;
        }

        if(contents == null) {
            contents = new ArrayList<List<String>>();
        }

        contents.add(content);
    }

    public int getHeaderColumnSize() {
        return headerColumnSize;
    }

    public boolean hasContent() {
        return CollectionUtils.isNotEmpty(contents);
    }

    public String getHeader(int index) {
        return headers.get(index);
    }

    public List<Integer> buildContentIndices() {
        List<Integer> indices = new ArrayList<Integer>(contents.size());

        for(int i = 0; i < contents.size(); i++) {
            indices.add(i);
        }

        return indices;
    }

    public int getSize() {
        return CollectionUtils.size(contents);
    }

    public String getCSVContent() throws IOException {
        StringWriter str = new StringWriter();
        CSVWriter writer = new CSVWriter(str);

        try {
            for(List<String> content : contents) {
                List<String> line = content.subList(headerColumnSize - 1, content.size());
                writer.writeNext(line.toArray(new String[line.size()]));
            }
        } finally {
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(str);
        }

        return str.toString();
    }

    public String getContent(int contentIndex, int contentColumnIndex) {
        return contents.get(contentIndex).get(contentColumnIndex + (headerColumnSize - 1));
    }

    public List<String> getContentAsList(int contentColumnIndexKey, String key, int contentColumnIndexValue) {
        List<String> contentList = new ArrayList<String>();

        if(CollectionUtils.isNotEmpty(contents)) {
            for(List<String> content : contents) {
                if(content.get(contentColumnIndexKey + (headerColumnSize - 1)).equals(key)) {
                    contentList.add(content.get(contentColumnIndexValue + (headerColumnSize - 1)));
                }
            }
        }

        return contentList;
    }
}
