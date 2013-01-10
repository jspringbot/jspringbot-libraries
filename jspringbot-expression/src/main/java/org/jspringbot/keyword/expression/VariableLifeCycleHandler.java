package org.jspringbot.keyword.expression;

import org.jspringbot.MainContextHolder;
import org.jspringbot.Visitor;
import org.jspringbot.keyword.expression.plugin.DefaultVariableProviderImpl;
import org.jspringbot.lifecycle.LifeCycleAdapter;
import org.jspringbot.spring.RobotScope;
import org.jspringbot.spring.SpringRobotLibraryManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class VariableLifeCycleHandler extends LifeCycleAdapter {
    private static final String DEFAULT_VARIABLE_PROVIDER_BEAN_NAME = "defaultVariableProvider";

    @Override
    public void startTest(String name, Map attributes) {
        if(MainContextHolder.isEnabled()) {
            SpringRobotLibraryManager manager = MainContextHolder.get().getBean(SpringRobotLibraryManager.class);

            manager.visitActive(RobotScope.ALL, new Visitor<ClassPathXmlApplicationContext>() {
                @Override
                public void visit(ClassPathXmlApplicationContext context) {
                    if(context.containsBean(DEFAULT_VARIABLE_PROVIDER_BEAN_NAME)) {
                        DefaultVariableProviderImpl provider = (DefaultVariableProviderImpl) context.getBean(DEFAULT_VARIABLE_PROVIDER_BEAN_NAME);

                        provider.clear();
                    }
                }
            });
        }
    }
}
