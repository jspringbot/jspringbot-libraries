package org.jspringbot.keyword.expression.plugin;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;

import java.util.Map;

public class ExpressionHandlerManager {

    private ApplicationContext context;

    private ExpressionHandler defaultHandler;

    public ExpressionHandlerManager(ApplicationContext context, ExpressionHandler defaultHandler) {
        this.context = context;
        this.defaultHandler = defaultHandler;
    }

    public Object defaultEvaluation(String expression) throws Exception {
        return defaultHandler.evaluate(expression);
    }

    public Object evaluation(String prefix, String expression) throws Exception {
        Map<String, ExpressionHandlerRegistryBean> handlers = context.getBeansOfType(ExpressionHandlerRegistryBean.class);

        for(ExpressionHandlerRegistryBean bean : handlers.values()) {
            if(StringUtils.equals(bean.getHandler().getPrefix(), prefix)) {
                return bean.getHandler().evaluate(expression);
            }
        }

        return defaultEvaluation(prefix + ":" + expression);
    }
}
