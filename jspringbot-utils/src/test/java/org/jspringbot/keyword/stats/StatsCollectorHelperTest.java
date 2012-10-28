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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//@Ignore
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-utils.xml"})
public class StatsCollectorHelperTest {

    @Autowired
    public StatsCollectorHelper statsCollectorHelper;

    @Test
    public void testPercentageStats() {
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page2");
        statsCollectorHelper.includeStat("group1_page2");
        statsCollectorHelper.includeStat("group1_page3");
        statsCollectorHelper.includeStat("group1_page4");

        statsCollectorHelper.percentageShouldBe("group1_page1", 60);
        statsCollectorHelper.percentageShouldBe("group1_page2", 20);
        statsCollectorHelper.percentageShouldBe("group1_page3", 10);
        statsCollectorHelper.percentageShouldBe("group1_page4", 10);
    }

    @Test
    public void testPercentageStatsInRange() {
        statsCollectorHelper.reset();
        statsCollectorHelper.includeStat("group1_page1");
        statsCollectorHelper.includeStat("group1_page2");
        statsCollectorHelper.includeStat("group1_page3");

        statsCollectorHelper.percentageShouldBeInRange("group1_page1", 32, 34);
        statsCollectorHelper.percentageShouldBeInRange("group1_page2", 32, 34);
        statsCollectorHelper.percentageShouldBeInRange("group1_page3", 32, 34);
    }

}
