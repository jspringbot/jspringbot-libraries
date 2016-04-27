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

package org.jspringbot.keyword.map;

import org.jspringbot.Keyword;
import org.jspringbot.KeywordInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Map Set Random Concat", description = "Gets a random concatenated string", parameters = {"key","delimeter","size"})
public class MapSetRandomConcat implements Keyword {

    @Autowired
    protected MapSetHelper mapSetHelper;

    @Override
    public Object execute(Object[] params) {

        String key = String.valueOf(params[0]);
        String delimeter = String.valueOf(params[1]);
        int size = Integer.parseInt(String.valueOf(params[2]));

        return mapSetHelper.randomConcat(key, delimeter, size);
    }
}
