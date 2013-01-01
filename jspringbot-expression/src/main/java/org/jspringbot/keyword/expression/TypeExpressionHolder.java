package org.jspringbot.keyword.expression;

import java.util.Stack;

class TypeExpressionHolder {
    private static final ThreadLocal<Stack<Class>> EXPECTED_TYPE = new ThreadLocal<Stack<Class>>() {{
        set(new Stack<Class>());
    }};

    static void push(Class clazz) {
        EXPECTED_TYPE.get().push(clazz);
    }

    static Class peek() {
        if(!EXPECTED_TYPE.get().isEmpty()) {
            return EXPECTED_TYPE.get().peek();
        }

        return Object.class;
    }

    static Class pop() {
        if(!EXPECTED_TYPE.get().isEmpty()) {
            return EXPECTED_TYPE.get().pop();
        }

        return Object.class;
    }

    static void remove() {
        EXPECTED_TYPE.remove();
    }
}
