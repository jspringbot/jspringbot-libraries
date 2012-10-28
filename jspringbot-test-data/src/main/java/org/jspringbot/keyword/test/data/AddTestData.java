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

package org.jspringbot.keyword.test.data;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@KeywordInfo(name = "Add Test Data",
        description = "Add Test Data values. \n\n" +
                      "See `Introduction`",
        parameters = {"*headers"})
public class AddTestData extends AbstractTestDataKeyword {

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Object[] params) {
        try {
            List<String> list = new ArrayList<String>();
            for(Object param : params) {
                list.add(String.valueOf(param));
            }

            helper.addTestData(list);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return null;
    }
}
