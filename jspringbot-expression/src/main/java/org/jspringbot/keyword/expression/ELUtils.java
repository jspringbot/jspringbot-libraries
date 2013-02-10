package org.jspringbot.keyword.expression;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jspringbot.MainContextHolder;
import org.jspringbot.PythonUtils;
import org.jspringbot.keyword.expression.plugin.VariableProviderManager;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.python.util.PythonInterpreter;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ELUtils {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    public static final Pattern PATTERN = Pattern.compile("\\$\\{([^\\}]+)\\}", Pattern.CASE_INSENSITIVE);

    public static String resource(String resourceAsText) throws Exception {
        ResourceEditor editor = new ResourceEditor();
        editor.setAsText(resourceAsText);

        Resource resource = (Resource) editor.getValue();
        String resourceString = IOUtils.toString(resource.getInputStream());

        return replaceVars(resourceString);
    }

    public static String md5(String str) throws NoSuchAlgorithmException {
        MessageDigest digest = MessageDigest.getInstance("MD5");
        byte[] data = str.getBytes();
        digest.update(data,0,data.length);
        BigInteger i = new BigInteger(1,digest.digest());

        return i.toString(16);
    }

    private static ExpressionHelper getHelper() {
        return ApplicationContextHolder.get().getBean(ExpressionHelper.class);
    }

    private static VariableProviderManager getVariables() {
        return new VariableProviderManager(ApplicationContextHolder.get());
    }

    public static String concatMillis(String name) {
        return name + System.currentTimeMillis();
    }

    public static String replaceVars(String string) throws Exception {
        StringBuilder buf = new StringBuilder(string);
        Matcher matcher = PATTERN.matcher(buf);

        int startIndex = 0;
        while(startIndex < buf.length() && matcher.find(startIndex)) {
            String name = matcher.group(1);

            Object value = getVariables().getVariables().get(name);
            LOG.keywordAppender().appendProperty("Replacement EL Value ['" + name + "']", value);
            if(value == null) {
                value = robotVar(name);
                LOG.keywordAppender().appendProperty("Replacement Robot Value ['" + name + "']", value);
            }

            buf.replace(matcher.start(), matcher.end(), String.valueOf(value));
            startIndex = matcher.end();
        }

        LOG.keywordAppender().appendProperty(String.format("Replacement [%s]", string), buf.toString());


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

    public static boolean in(String... strs) {
        List<String> list = Arrays.asList(strs).subList(1,  strs.length);

        return list.contains(strs[0]);
    }

    public static String substring(String str, Integer... index) {
        if(index.length > 1) {
            return StringUtils.substring(str, index[0], index[1]);
        } else if(index.length == 1) {
            return StringUtils.substring(str, index[0]);
        }

        throw new IllegalArgumentException("No startIndex provided.");
    }

    public static Double parseDouble(String str, String format) throws ParseException {
        DecimalFormat formatter = new DecimalFormat(format);

        return formatter.parse(str).doubleValue();
    }

    public static String formatDouble(Double amount, String format) {
        DecimalFormat formatter = new DecimalFormat(format);

        return formatter.format(amount);
    }
}
