package org.jspringbot.keyword.expression;

import java.util.Stack;

class TypeExpressionHolder {
    private static final ThreadLocal<Stack<Class>> EXPECTED_TYPE = new ThreadLocal<Stack<Class>>();

    static Stack<Class> get() {
        Stack<Class> stack = EXPECTED_TYPE.get();
        if (stack == null) {
            stack = new Stack<Class>();
            EXPECTED_TYPE.set(stack);
        }

        return stack;
    }

    static void push(Class clazz) {
        get().push(clazz);
    }

    static Class peek() {
        if(!get().isEmpty()) {
            return get().peek();
        }

        return Object.class;
    }

    static Class pop() {
        if(!get().isEmpty()) {
            return get().pop();
        }

        return Object.class;
    }

    static void remove() {
        EXPECTED_TYPE.remove();
    }
}
