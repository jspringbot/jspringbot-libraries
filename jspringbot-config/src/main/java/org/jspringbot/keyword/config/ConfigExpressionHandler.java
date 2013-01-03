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
    protected Object evaluateInternal(String expression, String[] tokens) {
        LOG.keywordAppender().appendProperty("Expression Handler", "jSpringBot Config");

        if(tokens.length == 1) {
            return helper.getProperty(tokens[0]);
        }

        if(tokens.length == 2) {
            ConfigDomainObject domain = helper.createDomainObjectInternal(tokens[0]);

            return domain.get(tokens[1]);
        }

        throw new IllegalArgumentException(String.format("Syntax error for config type '%s' expression.", expression));
    }
}
