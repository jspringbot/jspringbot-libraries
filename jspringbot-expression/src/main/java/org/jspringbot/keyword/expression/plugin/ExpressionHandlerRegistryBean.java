package org.jspringbot.keyword.expression.plugin;

public class ExpressionHandlerRegistryBean {
    private ExpressionHandler handler;

    public ExpressionHandlerRegistryBean(ExpressionHandler handler) {
        this.handler = handler;
    }

    public ExpressionHandler getHandler() {
        return handler;
    }
}
