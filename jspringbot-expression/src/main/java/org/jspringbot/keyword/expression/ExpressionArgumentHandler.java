package org.jspringbot.keyword.expression;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.argument.ArgumentHandler;

public class ExpressionArgumentHandler implements ArgumentHandler {

    private ExpressionHelper helper;

    public ExpressionArgumentHandler(ExpressionHelper helper) {
        this.helper = helper;
    }

    @Override
    public boolean isSupported(String keyword, Object parameter) {
        return !StringUtils.startsWith(keyword, "Evaluate Expression") && String.class.isInstance(parameter) && helper.isSupported((String) parameter);
    }

    @Override
    public Object handle(Object parameter) {
        String expression = (String) parameter;
        try {
            return helper.evaluate(expression);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Unable to evaluate expression '%s'.", expression));
        }
    }
}
