package org.jspringbot.keyword.expression;

import org.jspringbot.keyword.expression.plugin.DefaultVariableProviderImpl;
import org.jspringbot.lifecycle.LifeCycleAdapter;
import org.jspringbot.spring.ApplicationContextHolder;

import java.util.Map;

public class VariableLifeCycleHandler extends LifeCycleAdapter {
    private static DefaultVariableProviderImpl getDefaultVariable() {
        if(ApplicationContextHolder.get() == null) {
            return null;
        }

        return (DefaultVariableProviderImpl) ApplicationContextHolder.get().getBean("defaultVariableProvider");
    }

    @Override
    public void startTest(String name, Map attributes) {
        DefaultVariableProviderImpl variables = getDefaultVariable();

        if(variables != null) {
            variables.clear();
        }
    }
}
