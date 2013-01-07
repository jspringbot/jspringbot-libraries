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

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.jspringbot.keyword.expression.ELUtils;
import org.jspringbot.keyword.expression.ExpressionHelper;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ConfigHelper {
    private static final Logger LOGGER = Logger.getLogger(ConfigHelper.class);

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ConfigHelper.class);

    private Map<String, Properties> domainProperties = new HashMap<String, Properties>();

    private String selectedDomain;

    public boolean hasDomain(String domain) {
        return domainProperties.containsKey(domain);
    }

    public String getSelectedDomain() {
        return selectedDomain;
    }

    private void addProperties(String domain, File file) throws IOException {
        String filename = file.getName();

        Properties properties = new Properties();

        if(StringUtils.endsWith(filename, ".properties")) {
            properties.load(new FileReader(file));
        } else if(StringUtils.endsWith(filename, ".xml")) {
            properties.loadFromXML(new FileInputStream(file));
        }

        domainProperties.put(domain, properties);
    }

    public void init() throws IOException {
        ResourceEditor editor = new ResourceEditor();
        editor.setAsText("classpath:config/");
        Resource configDirResource = (Resource) editor.getValue();

        boolean hasConfigDirectory = true;
        boolean hasConfigProperties = true;

        if(configDirResource != null) {
            try {
                File configDir = configDirResource.getFile();

                if(configDir.isDirectory()) {
                    File[] propertiesFiles = configDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            return StringUtils.endsWith(file.getName(), ".properties") || StringUtils.endsWith(file.getName(), ".xml");
                        }
                    });

                    for(File propFile : propertiesFiles) {
                        String filename = propFile.getName();
                        String name = StringUtils.substring(filename, 0, StringUtils.indexOf(filename, "."));

                        addProperties(name, propFile);
                    }
                }
            } catch(IOException e) {
                hasConfigDirectory = false;
            }
        }

        editor.setAsText("classpath:config.properties");
        Resource configPropertiesResource = (Resource) editor.getValue();


        if(configPropertiesResource != null) {
            try {
                File configPropertiesFile = configPropertiesResource.getFile();

                if(configPropertiesFile.isFile()) {
                    Properties configs = new Properties();

                    configs.load(new FileReader(configPropertiesFile));

                    for(Map.Entry entry : configs.entrySet()) {
                        String name = (String) entry.getKey();
                        editor.setAsText(String.valueOf(entry.getValue()));

                        try {
                            Resource resource = (Resource) editor.getValue();
                            addProperties(name, resource.getFile());
                        } catch(Exception e) {
                            throw new IOException(String.format("Unable to load config '%s'.", name), e);
                        }
                    }
                }
            } catch(IOException e) {
                hasConfigProperties = false;
            }
        }

        if(!hasConfigDirectory && !hasConfigProperties) {
            LOGGER.warn("No configuration found.");
        }
    }

    public void selectDomain(String selectedDomain) {
        LOG.keywordAppender().appendProperty("Selected Domain", selectedDomain);

        if (!domainProperties.containsKey(selectedDomain)) {
            throw new IllegalArgumentException(String.format("Unsupported selected domain '%s'", selectedDomain));
        }

        this.selectedDomain = selectedDomain;
    }

    ConfigDomainObject createDomainObjectInternal() {
        return createDomainObjectInternal(selectedDomain);
    }

    ConfigDomainObject createDomainObjectInternal(String selectedDomain) {
        if (!domainProperties.containsKey(selectedDomain)) {
            throw new IllegalArgumentException(String.format("Unsupported selected domain '%s'", selectedDomain));
        }

        return new ConfigDomainObject(selectedDomain, domainProperties.get(selectedDomain));
    }

    public ConfigDomainObject createDomainObject(String selectedDomain) {
        LOG.keywordAppender().appendProperty("Domain", selectedDomain);

        return createDomainObjectInternal(selectedDomain);
    }

    public Boolean getBooleanProperty(String key) {
        Boolean value = Boolean.valueOf(getProperty(key));

        LOG.keywordAppender().appendProperty("Property Boolean Value", value);

        return value;
    }

    public Long getLongProperty(String key) {
        Long value = Long.valueOf(getProperty(key));

        LOG.keywordAppender().appendProperty("Property Long Value", value);

        return value;
    }

    public Integer getIntegerProperty(String key) {
        Integer value = Integer.valueOf(getProperty(key));

        LOG.keywordAppender().appendProperty("Property Integer Value", value);

        return value;
    }

    public Double getDoubleProperty(String key) {
        Double value = Double.valueOf(getProperty(key));

        LOG.keywordAppender().appendProperty("Property Double Value", value);

        return value;
    }

    public static String evaluate(String value) {
        try {
            // ensure that expression is enabled
            ApplicationContextHolder.get().getBean(ExpressionHelper.class);

            return ELUtils.replaceVars(value);
        } catch (Exception e) {
            LOGGER.warn(e.getMessage(), e);

            return value;
        }
    }

    public String getProperty(String key) {
        LOG.keywordAppender()
                .appendProperty("Current Selected Domain", selectedDomain)
                .appendProperty("Property Key", key);

        if (selectedDomain == null) {
            throw new IllegalStateException("No domain selected");
        }

        Properties properties = domainProperties.get(selectedDomain);

        if (!properties.containsKey(key)) {
            throw new IllegalArgumentException(String.format("No property found for key '%s'", key));
        }

        LOG.keywordAppender().appendProperty("Property String Value", properties.getProperty(key));

        return evaluate(properties.getProperty(key));
    }
}
