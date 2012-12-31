package org.jspringbot.keyword.expression;

import org.jspringbot.spring.ApplicationContextHolder;

public class EvaluateExpressionUtils {

    private static ExpressionHelper getHelper() {
        if(ApplicationContextHolder.get() == null) {
            throw new IllegalStateException("Not under jSpringBot context.");
        }

        return ApplicationContextHolder.get().getBean(ExpressionHelper.class);
    }

    public static Object eval(String expression) throws Exception {
        return getHelper().evaluate(expression);
    }
}
