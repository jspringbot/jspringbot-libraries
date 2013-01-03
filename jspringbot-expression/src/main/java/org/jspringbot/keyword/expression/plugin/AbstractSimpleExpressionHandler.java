package org.jspringbot.keyword.expression.plugin;

import org.apache.commons.lang.StringUtils;

public abstract class AbstractSimpleExpressionHandler implements ExpressionHandler {

    @Override
    public abstract String getPrefix();

    @Override
    public Object evaluate(String expression) throws Exception {
        return evaluateInternal(expression, StringUtils.split(expression, ':'));
    }

    protected abstract Object evaluateInternal(String expression, String[] tokens) throws Exception;
}
