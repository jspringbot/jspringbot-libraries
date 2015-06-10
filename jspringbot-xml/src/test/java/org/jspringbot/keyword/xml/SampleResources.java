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

package org.jspringbot.keyword.xml;

import org.apache.commons.io.IOUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;

public class SampleResources {
    private Resource sampleXML;

    private Resource sample2XML;

    public Resource getSample2XML() {
        return sample2XML;
    }

    public void setSample2XML(Resource sample2XML) {
        this.sample2XML = sample2XML;
    }

    public Resource getSampleXML() {
        return sampleXML;
    }

    public void setSampleXML(Resource sampleXML) {
        this.sampleXML = sampleXML;
    }

    public String getSampleXMLString() throws IOException {
        InputStream in = null;

        try {
            in = sampleXML.getInputStream();
            return IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }

    public String getSample2XMLString() throws IOException {
        InputStream in = null;

        try {
            in = sample2XML.getInputStream();
            return IOUtils.toString(in);
        } finally {
            IOUtils.closeQuietly(in);
        }
    }
}
