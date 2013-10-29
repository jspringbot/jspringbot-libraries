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

package org.jspringbot.keyword.selenium.flex;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Flex Ensure Data Grid Row Count",
        parameters = {"widgetId", "expectedRowCount"},
        description = "classpath:desc/FlexEnsureDataGridRowCount.txt"
)
public class FlexEnsureDataGridRowCount extends AbstractFlexSeleniumKeyword {

    @Override
    public Object execute(Object[] params) {
        driver.ensureDataGridRowCount(String.valueOf(params[0]), Integer.valueOf(String.valueOf(params[1])));

        return null;
    }
}
