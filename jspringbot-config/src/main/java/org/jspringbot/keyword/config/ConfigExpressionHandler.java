package org.jspringbot.keyword.config;

import org.jspringbot.keyword.expression.plugin.AbstractSimpleExpressionHandler;
import org.jspringbot.syntax.HighlightRobotLogger;

public class ConfigExpressionHandler extends AbstractSimpleExpressionHandler {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ConfigExpressionHandler.class);

    public static final String EXPRESSION_PREFIX = "config";

    private ConfigHelper helper;

    public ConfigExpressionHandler(ConfigHelper helper) {
        this.helper = helper;
    }

    @Override
    public String getPrefix() {
        return EXPRESSION_PREFIX;
    }

    @Override
    protected Object evaluateInternal(String[] tokens) {
        LOG.keywordAppender().appendProperty("Expression Handler", "jSpringBot Config");

        if(tokens.length == 1) {
            return helper.getProperty(tokens[0]);
        }

        if(tokens.length == 2) {
            ConfigDomainObject domain = helper.createDomainObjectInternal();
            if(helper.hasDomain(tokens[0])) {
                domain = helper.createDomainObjectInternal(tokens[0]);

                return domain.get(tokens[1]);
            } else {
                return getForType(domain, tokens[1], tokens[0]);
            }
        }

        if(tokens.length == 3) {
            ConfigDomainObject domain = helper.createDomainObjectInternal(tokens[0]);

            return getForType(domain, tokens[2], tokens[1]);
        }

        throw new IllegalArgumentException("Syntax error for config type expression.");
    }

    private Object getForType(ConfigDomainObject domain, String type, String property) {
        if(type.equalsIgnoreCase("integer")) {
            return domain.getInteger(property);
        } else if(type.equalsIgnoreCase("long")) {
            return domain.getLong(property);
        } else if(type.equalsIgnoreCase("boolean")) {
            return domain.getBoolean(property);
        } else if(type.equalsIgnoreCase("double")) {
            return domain.getDouble(property);
        }

        throw new IllegalArgumentException(String.format("Invalid config type '%s'", type));
    }
}
