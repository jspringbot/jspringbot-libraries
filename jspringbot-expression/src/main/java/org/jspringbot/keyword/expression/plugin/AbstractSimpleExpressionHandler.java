package org.jspringbot.keyword.expression.plugin;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractSimpleExpressionHandler implements ExpressionHandler {

    @Override
    public abstract String getPrefix();

    @Override
    public Object evaluate(String expression) {
        return evaluateInternal(StringUtils.split(expression, ':'));
    }

    protected abstract Object evaluateInternal(String[] tokens);
}
