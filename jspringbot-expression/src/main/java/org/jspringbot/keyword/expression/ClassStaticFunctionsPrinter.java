package org.jspringbot.keyword.expression;

import java.io.PrintStream;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ClassStaticFunctionsPrinter {

    private static final Pattern PATTERN = Pattern.compile("(.*)([0-9])+", Pattern.CASE_INSENSITIVE);

    private Class clazz;

    private Set<String> methodNames = new HashSet<String>();

    private String prefix;

    public ClassStaticFunctionsPrinter(Class clazz) {
        this.clazz = clazz;
    }

    public ClassStaticFunctionsPrinter addPrefix(String prefix) {
        this.prefix = prefix;

        return this;
    }

    private String getName(String name) {
        if(methodNames.contains(name)) {
            Matcher matcher = PATTERN.matcher(name);

            if(matcher.find()) {
                int current = Integer.parseInt(matcher.group(2));

                return getName(matcher.group(1) + (current  + 1));
            } else {
                return getName(name + 1);
            }
        }

        methodNames.add(name);

        return name;
    }

    public void print(PrintStream out) {
        out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

        out.println();
        out.println("<functions>");

        for(Method method : clazz.getMethods()) {
            String name = getName(method.getName());
            String signature = method.toString();

            if(!signature.startsWith("public static")) {
                continue;
            } else {
                signature = signature.substring(14);
                signature = signature.replace(clazz.getName() + ".", "");
            }
            out.println("  <function>");

            if(prefix != null) {
                out.println(String.format("  <prefix>%s</prefix>", prefix));
            }

            out.println(String.format("    <name>%s</name>", name));
            out.println(String.format("    <function-class>%s</function-class>", clazz.getName()));
            out.println(String.format("    <function-signature>%s</function-signature>", signature));
            out.println("  </function>");
        }

        out.println("</functions>");
    }
}
