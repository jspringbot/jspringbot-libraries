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

package org.jspringbot.keyword.expression.engine.function;

import org.apache.commons.lang.StringUtils;

import javax.el.FunctionMapper;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Default function mapper implementation
 */
class DefaultFunctionMapper extends FunctionMapper {

    private Map<String,Method> map = Collections.emptyMap();

    public void setFunction(String localName, Method method) {
        if (map.isEmpty()) {
            map = new HashMap<String,Method>();
        }

        map.put(localName, method);
    }

    public void setFunction(String prefix, String localName, Method method) {
        if(StringUtils.isNotBlank(prefix)) {
            setFunction(prefix + ":" + localName, method);
        } else {
            setFunction(localName, method);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Method resolveFunction(String prefix, String localName) {
        if(StringUtils.isBlank(prefix)) {
            return map.get(localName);
        }

        return map.get(prefix + ":" + localName);
    }
}
