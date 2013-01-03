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

import org.apache.commons.beanutils.MethodUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.propertyeditors.ClassEditor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class StaticMethodUtils {

    private static final Pattern METHOD_SIGNATURE_PATTERN = Pattern.compile("([a-z\\.\\[\\]]+)\\s+([a-z0-9$_]+)\\s*\\(\\s*(([a-z0-9$_\\[\\]\\._$]+(\\s*,\\s*)?)+)\\)", Pattern.CASE_INSENSITIVE);

    public static Method getMethod(Class clazz, String methodSignature) {
        Matcher matcher = METHOD_SIGNATURE_PATTERN.matcher(methodSignature);

        if(matcher.find()) {
            String methodName = matcher.group(2);
            String parameterTypes = matcher.group(3);

            List<Class> types = new ArrayList<Class>();
            for(String parameterType : StringUtils.split(parameterTypes, ',')) {
                ClassEditor editor = new ClassEditor();
                editor.setAsText(parameterType);
                types.add((Class) editor.getValue());
            }

            return MethodUtils.getAccessibleMethod(clazz, methodName, types.toArray(new Class[types.size()]));
        }

        throw new IllegalStateException(String.format("Invalid method signature '%s' found.", methodSignature));
    }

}
