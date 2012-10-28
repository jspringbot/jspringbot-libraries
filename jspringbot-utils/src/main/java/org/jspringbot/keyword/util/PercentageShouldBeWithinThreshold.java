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
@KeywordInfo(name = "Percentage Should Be Within Threshold", description = "Percentage Should Be Within Threshold.", parameters = {"expectedValue","actualValue","threshold"})
public class PercentageShouldBeWithinThreshold implements Keyword {

    @Override
    public Object execute(Object[] params) throws IllegalStateException {

        int expectedValue= Integer.parseInt(String.valueOf(params[0]));
        int actualValue= Integer.parseInt(String.valueOf(params[1]));
        int threshold= Integer.parseInt(String.valueOf(params[2]));

        int lowerLimit= expectedValue - threshold;
        int upperLimit= expectedValue + threshold;
        System.out.println("LowerLimit: " + lowerLimit + " <= Actual Value: " + actualValue + " <=" +  " UpperLimit: " + upperLimit);
        if ((lowerLimit > actualValue) || (actualValue > upperLimit)) {
            throw new IllegalArgumentException(String.format("Actual value: '%d' is not within '%d' threshold of expected value: '%d'", actualValue, threshold, expectedValue));
        }
        return null;
    }
}
