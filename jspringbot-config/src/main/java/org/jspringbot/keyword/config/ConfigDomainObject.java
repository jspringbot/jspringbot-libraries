package org.jspringbot.keyword.config;

import org.jspringbot.syntax.HighlightRobotLogger;

import java.util.Properties;

public class ConfigDomainObject {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ConfigDomainObject.class);

    private Properties properties;

    private String selectedDomain;

    public ConfigDomainObject(String selectedDomain, Properties properties) {
        this.properties = properties;
        this.selectedDomain = selectedDomain;
    }

    public String getDomain() {
        return selectedDomain;
    }

    public String get(String code) {
        LOG.keywordAppender()
                .appendProperty("Config Domain", selectedDomain)
                .appendProperty("Config Key", code);

        if (!properties.containsKey(code)) {
            throw new IllegalArgumentException(String.format("No property found for key '%s'", code));
        }

        LOG.keywordAppender()
                .appendProperty("Config String Value", properties.getProperty(code));

        return ConfigHelper.evaluate(properties.getProperty(code));
    }

    public Boolean getBoolean(String code) {
        return Boolean.valueOf(get(code));
    }

    public Integer getInteger(String code) {
        return Integer.valueOf(get(code));
    }

    public Long getLong(String code) {
        return Long.valueOf(get(code));
    }

    public Double getDouble(String code) {
        return Double.valueOf(get(code));
    }
}
