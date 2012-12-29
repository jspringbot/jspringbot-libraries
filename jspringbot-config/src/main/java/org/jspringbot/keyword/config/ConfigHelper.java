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

package org.jspringbot.keyword.config;

import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigHelper {

    protected Map<String, Resource> domains;

    protected Map<String, Properties> domainProperties;

    private String selectedDomain;

    public void setDomains(Map<String, Resource> domains) {
        this.domains = domains;
    }

    public void init() throws IOException {
        domainProperties = new HashMap<String, Properties>(domains.size());

        for (Map.Entry<String, Resource> entry : domains.entrySet()) {
            Properties properties = new Properties();
            properties.load(entry.getValue().getInputStream());

            domainProperties.put(entry.getKey(), properties);
        }
    }

    public void selectDomain(String selectedDomain) {
        if (!domainProperties.containsKey(selectedDomain)) {
            throw new IllegalArgumentException(String.format("Unsupported selected domain '%s'", selectedDomain));
        }

        this.selectedDomain = selectedDomain;
    }

    public ConfigDomainObject createDomainWrapper(String selectedDomain) {
        if (!domainProperties.containsKey(selectedDomain)) {
            throw new IllegalArgumentException(String.format("Unsupported selected domain '%s'", selectedDomain));
        }

        return new ConfigDomainObject(selectedDomain, domainProperties.get(selectedDomain));
    }

    public String getStringProperty(String key) {
        if (selectedDomain == null) {
            throw new IllegalStateException("No domain selected");
        }

        Properties properties = domainProperties.get(selectedDomain);

        if (!properties.containsKey(key)) {
            throw new IllegalArgumentException(String.format("No property found for key '%s'", key));
        }

        return properties.getProperty(key);
    }
}
