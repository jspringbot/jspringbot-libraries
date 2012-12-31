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

package org.jspringbot.keyword.expression.engine;

import org.jspringbot.keyword.expression.engine.function.SupportedFunctionsManager;

import javax.el.ELContext;
import javax.el.ELResolver;
import javax.el.FunctionMapper;
import javax.el.VariableMapper;
import java.util.Collections;
import java.util.Map;

/**
 * Default EL Context.
 */
public class DefaultELContext extends ELContext {

    private ELResolver resolver = new DefaultResolver();

    private DefaultVariableMapper variableMapper = new DefaultVariableMapper();

    private SupportedFunctionsManager manager;

    public DefaultELContext(SupportedFunctionsManager manager) {
        this(manager, Collections.<String, Object>emptyMap());
    }

    public DefaultELContext(SupportedFunctionsManager manager, Map<String, Object> variables) {
        this.manager = manager;
        for(Map.Entry<String, Object> entry : variables.entrySet()) {
            addVariable(entry.getKey(), entry.getValue());
        }
    }

    public void addVariable(String name, Object value) {
        resolver.setValue(this, null, name, value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ELResolver getELResolver() {
        return resolver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public FunctionMapper getFunctionMapper() {
        return manager.getFunctionMapper();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public VariableMapper getVariableMapper() {
        return variableMapper;
    }
}
