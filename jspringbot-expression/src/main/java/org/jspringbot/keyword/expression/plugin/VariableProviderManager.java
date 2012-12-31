package org.jspringbot.keyword.expression.plugin;

import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class VariableProviderManager {
    private Map<String, VariableProviderRegistryBean> handlers;

    public VariableProviderManager(ApplicationContext context) {
        handlers = context.getBeansOfType(VariableProviderRegistryBean.class);
    }

    public Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<String, Object>();
        for(VariableProviderRegistryBean bean : handlers.values()) {
            variables.putAll(bean.getHandler().getVariables());
        }

        return variables;
    }
}
