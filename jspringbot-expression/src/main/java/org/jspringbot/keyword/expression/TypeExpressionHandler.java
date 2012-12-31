package org.jspringbot.keyword.expression;

import org.jspringbot.keyword.expression.plugin.ExpressionHandler;

public class TypeExpressionHandler implements ExpressionHandler {

    private ExpressionHelper helper;

    private Class clazz;

    private String prefix;

    public TypeExpressionHandler(String prefix, ExpressionHelper helper, Class clazz) {
        this.prefix = prefix;
        this.helper = helper;
        this.clazz = clazz;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public Object evaluate(String expression) throws Exception {
        try {
            TypeExpressionHolder.set(clazz);
            return helper.evaluate(String.format("#{%s}", expression));
        } finally {
            TypeExpressionHolder.remove();
        }
    }

}
