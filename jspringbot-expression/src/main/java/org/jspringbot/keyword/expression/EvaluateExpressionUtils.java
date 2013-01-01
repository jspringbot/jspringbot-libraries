package org.jspringbot.keyword.expression;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.spring.ApplicationContextHolder;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class EvaluateExpressionUtils {

    private static ExpressionHelper getHelper() {
        if(ApplicationContextHolder.get() == null) {
            throw new IllegalStateException("Not under jSpringBot context.");
        }

        return ApplicationContextHolder.get().getBean(ExpressionHelper.class);
    }

    public static Object eval(final String expression, Object... args) throws Exception {
        if(args == null || args.length == 0) {
            return getHelper().evaluate(expression);
        }

        return getHelper().variableScope(Arrays.asList(args), new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return getHelper().evaluate(expression);
            }
        });
    }

    public static String join(String separator, Object... strs) {
        return StringUtils.join(strs, separator);
    }

    public static String concat(Object... strs) {
        return StringUtils.join(strs);
    }

    public static String substring(String str, Integer... index) {
        if(index.length > 1) {
            return StringUtils.substring(str, index[0], index[1]);
        } else if(index.length == 1) {
            return StringUtils.substring(str, index[0]);
        }

        throw new IllegalArgumentException("No startIndex provided.");
    }
}
