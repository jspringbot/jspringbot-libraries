package org.jspringbot.keyword.expression;

public class TypeExpressionHolder {
    private static final ThreadLocal<Class> EXPECTED_TYPE = new ThreadLocal<Class>();

    public static void set(Class clazz) {
        EXPECTED_TYPE.set(clazz);
    }

    public static Class get() {
        if(EXPECTED_TYPE.get() != null) {
            return EXPECTED_TYPE.get();
        }

        return Object.class;
    }

    public static void remove() {
        EXPECTED_TYPE.remove();
    }
}
