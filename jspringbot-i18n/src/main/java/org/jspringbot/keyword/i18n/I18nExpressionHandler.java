package org.jspringbot.keyword.i18n;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.keyword.expression.plugin.AbstractSimpleExpressionHandler;

public class I18nExpressionHandler extends AbstractSimpleExpressionHandler {
    public static final String EXPRESSION_PREFIX = "i18n";

    private I18nHelper helper;

    public I18nExpressionHandler(I18nHelper helper) {
        this.helper = helper;
    }

    @Override
    public String getPrefix() {
        return EXPRESSION_PREFIX;
    }

    @Override
    protected Object evaluateInternal(String[] tokens) {
        if(tokens.length == 1) {
            return helper.getMessage(tokens[0]);
        }

        if(tokens.length == 2) {
            if(StringUtils.equalsIgnoreCase(tokens[0], "locale")) {
                if(StringUtils.equalsIgnoreCase(tokens[1], "language")) {
                    return helper.getLanguage();
                } else if(StringUtils.equalsIgnoreCase(tokens[1], "country")) {
                    return helper.getCountry();
                } else if(StringUtils.equalsIgnoreCase(tokens[1], "displayCountry")) {
                    return helper.getDisplayCountry();
                } else if(StringUtils.equalsIgnoreCase(tokens[1], "displayLanguage")) {
                    return helper.getDisplayLanguage();
                }
            }

            return helper.getMessage(tokens[0], tokens[1]);
        }

        throw new IllegalArgumentException("Syntax error for i18 expression.");
    }
}
