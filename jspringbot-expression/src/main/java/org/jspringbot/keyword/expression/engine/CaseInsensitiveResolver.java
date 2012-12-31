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

import de.odysseus.el.util.SimpleResolver;
import org.apache.commons.lang.StringUtils;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.PropertyNotFoundException;
import javax.el.PropertyNotWritableException;

/**
 * Ensure that when property is not found will return null.
 * <p>
 * This also do case insensitive property resolving.
 * </p>
 */
public class CaseInsensitiveResolver extends SimpleResolver {

    @Override
    public void setValue(ELContext context, Object base, Object property, Object value) throws NullPointerException, PropertyNotFoundException, PropertyNotWritableException, ELException {
        if(String.class.isInstance(property)) {
            property = StringUtils.lowerCase(property.toString());
        }

        super.setValue(context, base, property, value);
    }

    @Override
    public Object getValue(ELContext context, Object base, Object property) throws ELException {
        try {
            if(String.class.isInstance(property)) {
                property = StringUtils.lowerCase(property.toString());
            }

            return super.getValue(context, base, property);
        } catch(PropertyNotFoundException e) {
            return null;
        }
    }
}
