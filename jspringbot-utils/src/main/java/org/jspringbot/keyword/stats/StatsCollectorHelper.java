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

package org.jspringbot.keyword.stats;

import java.util.HashMap;
import java.util.Map;

public class StatsCollectorHelper {

    protected Map<String,StatsInfo> statsInfoMap = new HashMap<String, StatsInfo>();

    public void reset() {
        statsInfoMap = new HashMap<String, StatsInfo>();
    }
    public void includeStat(String string) {
        StatsInfo statsInfo = statsInfoMap.get(string);
        if (statsInfo == null) {
            statsInfo = new StatsInfo();

            statsInfoMap.put(string, statsInfo);
        }

        statsInfo.add();
        computePercent();
    }

    public void percentageShouldBe(String string, int expectedPercentage) {
        StatsInfo statsInfo = statsInfoMap.get(string);
        if (expectedPercentage != statsInfo.getPercentage()) {
            throw new IllegalStateException(String.format("Expected percentage '%d%%' of '%s' should be equal to actual percentage '%d%%'", expectedPercentage, string, statsInfo.getPercentage()));
        }
    }

    public void percentageShouldBeInRange(String string, int expectedStartPercentage, int expectedEndPercentage) {
        StatsInfo statsInfo = statsInfoMap.get(string);
        if (statsInfo.getPercentage() < expectedStartPercentage || statsInfo.getPercentage() > expectedEndPercentage) {
            throw new IllegalStateException(String.format("Expected percentage '%d%%-%d%%' of '%s' should be equal to actual percentage '%d%%'", expectedStartPercentage, expectedEndPercentage, string, statsInfo.getPercentage()));
        }
    }

    private void computePercent() {
        int totalCount = 0;
        for (StatsInfo statsInfo: statsInfoMap.values()) {
            totalCount += statsInfo.getCount();
        }
        for (StatsInfo statsInfo: statsInfoMap.values()) {
            statsInfo.setTotal(totalCount);
        }
    }
}
