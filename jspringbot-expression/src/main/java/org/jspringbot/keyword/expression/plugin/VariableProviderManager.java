package org.jspringbot.keyword.expression.plugin;

import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

public class VariableProviderManager {
    private ApplicationContext context;

    public VariableProviderManager(ApplicationContext context) {
        this.context = context;
    }

    public Map<String, Object> getVariables() {
        Map<String, VariableProviderRegistryBean> handlers = context.getBeansOfType(VariableProviderRegistryBean.class);
        Map<String, Object> variables = new HashMap<String, Object>();
        for(VariableProviderRegistryBean bean : handlers.values()) {
            variables.putAll(bean.getHandler().getVariables());
        }

        return variables;
    }
}
