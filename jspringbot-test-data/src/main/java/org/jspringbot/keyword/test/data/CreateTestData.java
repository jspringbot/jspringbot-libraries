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

import org.apache.commons.lang.StringUtils;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@KeywordInfo(name = "Create Test Data",
        description = "To create a Test Data. \n\n" +
                      "See `Introduction`",
        parameters = {"name", "*headers"})
public class CreateTestData extends AbstractTestDataKeyword {

    @Override
    @SuppressWarnings("unchecked")
    public Object execute(Object[] params) {
        try {
            List<String> list = new ArrayList<String>();
            for(int  i = 1; i < params.length; i++) {
                list.add(String.valueOf(params[i]));
            }

            String file = String.valueOf(params[0]);
            if(StringUtils.startsWith(file, "file:")) {
                file = StringUtils.substringAfter(file, "file:");
            }

            helper.createTestData(new File(file), list);
        } catch (IOException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }

        return null;
    }
}
