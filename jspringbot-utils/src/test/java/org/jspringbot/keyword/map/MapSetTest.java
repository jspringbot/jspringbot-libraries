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

package org.jspringbot.keyword.map;

import org.junit.Test;

public class MapSetTest {

    protected MapSetHelper mapSet = new MapSetHelper();

    @Test
    public void testExecute() throws Exception {
        mapSet.reset();
        mapSet.add("key1","value1");
        mapSet.add("key2","value2");
        mapSet.add("key3","value3");
        mapSet.add("key1","value4");
        mapSet.add("key2","value5");
        mapSet.add("key3","value6");
        mapSet.add("key1","value7");
        mapSet.add("key2","value8");
        mapSet.add("key3","value9");
        mapSet.add("key1","value10");
        mapSet.add("key2","value11");
        mapSet.add("key3","value12");


        System.out.println(mapSet.randomConcat("key1", "|", 3));
        System.out.println(mapSet.randomConcat("key1", "|", 2));
        System.out.println(mapSet.randomConcat("key1", "|", 4));
    }
}
