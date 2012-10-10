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

package org.jspringbot.keyword.csv.criteria;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.Map;

/**
 * Add a column equals restriction
 */
public class ColumnEqualsRestriction implements Restriction {

    private Integer index;

    private String name;

    private String value;

    public ColumnEqualsRestriction(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ColumnEqualsRestriction(Integer index, String value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean matches(String[] line, Map<String, Integer> headers) {
        Validate.isTrue((MapUtils.isNotEmpty(headers) && name != null && headers.containsKey(name)) || index != null, "index or name is invalid.");

        if(name != null) {
            index = headers.get(name);
        }

        assert index != null;

        return StringUtils.equals(line[index], value);
    }
}
