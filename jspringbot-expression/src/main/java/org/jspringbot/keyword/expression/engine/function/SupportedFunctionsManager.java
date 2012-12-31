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

import com.thoughtworks.xstream.XStream;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.propertyeditors.ClassEditor;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Responsible for parsing {@code classpath:supported-functions.xml} to be used by function mapper.
 */
public final class SupportedFunctionsManager {

    private static final Logger LOGGER = Logger.getLogger(SupportedFunctionsManager.class);

    private DefaultFunctionMapper functionMapper = new DefaultFunctionMapper();

    private List<Function> registered = new LinkedList<Function>();

    public SupportedFunctionsManager(ApplicationContext context) {
        Map<String, SupportedFunctionRegistryBean> beans = context.getBeansOfType(SupportedFunctionRegistryBean.class);

        for(SupportedFunctionRegistryBean registryBean : beans.values()) {
            try {
                addResources(registryBean.getResources());
            } catch (IOException ignore) {
            }
        }
    }

    public void addResources(Resource[] resources) throws IOException {
        Validate.notEmpty(resources, "There should be available resources to add.");

        for(Resource resource : resources) {
            read(resource);
        }
    }

    public DefaultFunctionMapper getFunctionMapper() {
        return functionMapper;
    }

    public List<Function> getRegistered() {
        return registered;
    }

    private void read(Resource resource) throws IOException {
        XStream xstream = new XStream();
        xstream.processAnnotations(Functions.class);

        Functions functions = (Functions) xstream.fromXML(resource.getInputStream());
        for(Function function : functions.getFunctions()) {
            try {
                register(function);
                registered.add(function);
            } catch(Exception e) {
                LOGGER.warn(e.getMessage(), e);
            }
        }
    }

    private void register(Function function) {
        ClassEditor editor = new ClassEditor();
        editor.setAsText(function.getFunctionClass());

        Class clazz = (Class) editor.getValue();
        Method method = StaticMethodUtils.getMethod(clazz, function.getFunctionSignature());

        functionMapper.setFunction(function.getPrefix(), function.getName(), method);
    }
}
