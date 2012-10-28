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


import org.junit.Test;

public class UtilTest {

    @Test
    public void testComputePercentage() throws Exception {
        ComputePercentage computePercentage = new ComputePercentage();
        computePercentage.execute(new Object[]{"90","100"});
        computePercentage.execute(new Object[]{"50","100"});
        computePercentage.execute(new Object[]{"0","100"});
    }

    @Test
    public void testPercentageShouldBe() throws Exception {
        PercentageShouldBeWithinThreshold percentageShouldBe = new PercentageShouldBeWithinThreshold();
        percentageShouldBe.execute(new Object[]{"50","52","2"});
        percentageShouldBe.execute(new Object[]{"50","48","2"});
    }
}
