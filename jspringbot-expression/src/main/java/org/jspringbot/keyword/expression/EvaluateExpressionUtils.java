package org.jspringbot.keyword.expression;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.MainContextHolder;
import org.jspringbot.PythonUtils;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.python.util.PythonInterpreter;

import java.util.Arrays;
import java.util.concurrent.Callable;

public class EvaluateExpressionUtils {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);


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

    public static Object robotVar(String name) throws NoSuchFieldException, IllegalAccessException {
        if(MainContextHolder.get() == null) {
            throw new IllegalStateException("Not running on robot framework runtime.");
        }

        String robotVarName = String.format("\\${%s}", name);

        PythonInterpreter interpreter = MainContextHolder.get().getBean(PythonInterpreter.class);
        interpreter.set("name", robotVarName);
        interpreter.exec(
                "from robot.libraries.BuiltIn import BuiltIn\n" +
                "result= BuiltIn().get_variable_value(name)\n"
        );


        Object result = interpreter.get("result");

        if(result != null) {
            LOG.keywordAppender().appendProperty(String.format("robotVar('%s')", name), result.getClass());
        } else {
            LOG.keywordAppender().appendProperty(String.format("robotVar('%s')", name), null);
            return null;
        }

        Object javaObject = PythonUtils.toJava(result);

        if(javaObject != null) {
            LOG.keywordAppender().appendProperty(String.format("robotVar('%s')", name), javaObject.getClass());
        }

        LOG.keywordAppender().appendProperty(String.format("robotVar('%s')", name), javaObject);

        return javaObject;
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
