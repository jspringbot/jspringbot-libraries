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

package org.jspringbot.keyword.util;

import org.jspringbot.Keyword;
import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Compute Percentage", description = "Compute For Percentage.", parameters = {"count","totalCount"})
public class ComputePercentage implements Keyword {

    @Override
    public Object execute(Object[] params) {

        double count= Double.parseDouble(String.valueOf(params[0]));
        double totalCount= Double.parseDouble(String.valueOf(params[1]));

        if (totalCount == 0) {
            throw new IllegalArgumentException("Total count should not be 0");
        }

        int percentage = (int) ((count/totalCount) * 100);
        System.out.println("Percentage: " + ((count/totalCount) * 100));

        return ((Integer) percentage).toString();
    }
}
