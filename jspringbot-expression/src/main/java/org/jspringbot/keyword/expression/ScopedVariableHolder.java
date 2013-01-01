package org.jspringbot.keyword.expression;

import java.util.Collections;
import java.util.Map;
import java.util.Stack;

class ScopedVariableHolder {
    private static final ThreadLocal<Stack<Map<String, Object>>> EXPECTED_TYPE = new ThreadLocal<Stack<Map<String, Object>>>() {{
        set(new Stack<Map<String, Object>>());
    }};

    static void push(Map<String, Object> scopedVariable) {
        if(!EXPECTED_TYPE.get().isEmpty() && !EXPECTED_TYPE.get().peek().isEmpty()) {
            scopedVariable.putAll(EXPECTED_TYPE.get().peek());
        }

        EXPECTED_TYPE.get().push(scopedVariable);
    }

    static Map<String, Object> peek() {
        if(!EXPECTED_TYPE.get().isEmpty()) {
            return EXPECTED_TYPE.get().peek();
        }

        return Collections.emptyMap();
    }

    static Map<String, Object> pop() {
        if(!EXPECTED_TYPE.get().isEmpty()) {
            return EXPECTED_TYPE.get().pop();
        }

        return Collections.emptyMap();
    }
}
