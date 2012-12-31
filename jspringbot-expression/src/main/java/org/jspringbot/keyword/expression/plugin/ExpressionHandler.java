package org.jspringbot.keyword.expression.plugin;

public interface ExpressionHandler {

    String getPrefix();

    Object evaluate(String expression) throws Exception;
}
