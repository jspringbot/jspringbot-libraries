package org.jspringbot.keyword.expression;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.MainContextHolder;
import org.jspringbot.PythonUtils;
import org.jspringbot.keyword.expression.plugin.DefaultVariableProviderImpl;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.python.util.PythonInterpreter;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ELUtils {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    public static final Pattern PATTERN = Pattern.compile("\\$\\{[^\\}]+\\}", Pattern.CASE_INSENSITIVE);

    private static ExpressionHelper getHelper() {
        return ApplicationContextHolder.get().getBean(ExpressionHelper.class);
    }

    private static DefaultVariableProviderImpl getVariables() {
        return (DefaultVariableProviderImpl) ApplicationContextHolder.get().getBean("defaultVariableProvider");
    }

    public static String replaceVars(String string) throws Exception {
        StringBuilder buf = new StringBuilder(string);
        Matcher matcher = PATTERN.matcher(buf);

        int startIndex = 0;
        while(matcher.find(startIndex)) {
            String name = matcher.group(1);

            String value = String.valueOf(getVariables().getVariables().get(name));
            if(value == null) {
                value = String.valueOf(robotVar(name));
            }

            buf.replace(matcher.start(), matcher.end(), value);
            startIndex = matcher.end();
        }

        return buf.toString();
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
