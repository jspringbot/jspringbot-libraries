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

package org.jspringbot.keyword.selenium;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@KeywordInfo(
        name = "Unselect From List By Index",
        parameters = {"locator", "*indices"},
        description = "classpath:desc/UnselectFromListByIndex.txt"
)
public class UnselectFromListByIndex extends AbstractSeleniumKeyword {

    @Override
    public Object execute(Object[] params) {

        List<Integer> list = new ArrayList<Integer>();
        for(int  i = 1; i < params.length; i++) {
            list.add(Integer.valueOf(String.valueOf(params[i])));
        }

        helper.selectFromListByIndex(String.valueOf(params[0]), list);

        return null;
    }
}
