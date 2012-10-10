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

package org.jspringbot.keyword.http;


import org.jspringbot.keyword.json.JSONHelper;
import org.jspringbot.keyword.xml.XMLHelper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:sample-http.xml"})
public class HTTPHelperTest {

    @Autowired
    private HTTPHelper httpHelper;

    @Autowired
    private XMLHelper xmlHelper;

    @Autowired
    private JSONHelper jsonHelper;

    @Test
    public void testWundergroundXMLApi() throws  Exception{
        httpHelper.createGetRequest("http://api.wunderground.com/api/78aa96563b9ec435/conditions/q/CA/San_Francisco.xml");
        httpHelper.invokeRequest();

        httpHelper.responseStatusCodeShouldBeEqualTo(200);
        httpHelper.responseShouldBeXML();

        assertEquals("text/xml; charset=UTF-8", httpHelper.getResponseHeader("Content-Type"));

        // simple xml testing
        xmlHelper.xpathTextContentShouldBeEqual("//response/current_observation/display_location/city", "San Francisco");
        xmlHelper.xpathTextContentShouldBeEqual("//response/current_observation/display_location/state", "CA");
    }

    @Test
    public void testWundergroundJSONApi() throws  Exception{
        httpHelper.createGetRequest("http://api.wunderground.com/api/78aa96563b9ec435/conditions/q/CA/San_Francisco.json");
        httpHelper.invokeRequest();

        httpHelper.responseStatusCodeShouldBeEqualTo(200);
        httpHelper.responseShouldBeJson();

        assertEquals("application/json; charset=UTF-8", httpHelper.getResponseHeader("Content-Type"));

        // simple json testing
        jsonHelper.jsonValueShouldBe("current_observation.display_location.city", "San Francisco");
        jsonHelper.jsonValueShouldBe("current_observation.display_location.state", "CA");
    }
}
