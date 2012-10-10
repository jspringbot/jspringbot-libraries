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

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Will or all provided restrictions
 */
public class DisjunctionRestriction implements Restriction, RestrictionAppender {

    private List<Restriction> restrictions = new LinkedList<Restriction>();

    public DisjunctionRestriction or(Restriction restriction) {
        restrictions.add(restriction);

        return this;
    }


    @Override
    public boolean matches(String[] line, Map<String, Integer> headers) {
        for(Restriction restriction : restrictions) {
            if(restriction.matches(line, headers)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void append(Restriction r) {
        or(r);
    }
}
