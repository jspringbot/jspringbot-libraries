package org.jspringbot.keyword.expression.plugin;

public class VariableProviderRegistryBean {
    private VariableProvider handler;

    public VariableProviderRegistryBean(VariableProvider handler) {
        this.handler = handler;
    }

    public VariableProvider getHandler() {
        return handler;
    }
}
