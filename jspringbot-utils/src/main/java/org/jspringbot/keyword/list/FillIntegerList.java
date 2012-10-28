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

package org.jspringbot.keyword.list;

import org.jspringbot.Keyword;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@KeywordInfo(name = "Fill Integer List", description = "Fill Integer List.", parameters = {"start","end"})
public class FillIntegerList implements Keyword {

    @Override
    public Object execute(Object[] params) {
        int start = Integer.parseInt(String.valueOf(params[0]));
        int end = Integer.parseInt(String.valueOf(params[1]));

        List<Integer> list = new ArrayList<Integer>();
        for(int i = start; i < end+1; i++) {
            list.add(i);
        }

        return list;
    }
}
