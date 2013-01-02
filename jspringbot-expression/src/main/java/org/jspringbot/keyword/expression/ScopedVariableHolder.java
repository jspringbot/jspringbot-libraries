package org.jspringbot.keyword.expression;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.Collections;
import java.util.Map;
import java.util.Stack;

class ScopedVariableHolder {
    private static final ThreadLocal<Stack<Map<String, Object>>> EXPECTED_TYPE = new ThreadLocal<Stack<Map<String, Object>>>();

    static Stack<Map<String, Object>> get() {
        Stack<Map<String, Object>> stack = EXPECTED_TYPE.get();
        if (stack == null) {
            stack = new Stack<Map<String, Object>>();
            EXPECTED_TYPE.set(stack);
        }

        return stack;
    }

    static void push(Map<String, Object> scopedVariable) {
        if(CollectionUtils.isNotEmpty(get()) && MapUtils.isNotEmpty(get().peek())) {
            scopedVariable.putAll(get().peek());
        }

        get().push(scopedVariable);
    }

    static Map<String, Object> peek() {
        if(!get().isEmpty()) {
            return get().peek();
        }

        return Collections.emptyMap();
    }

    static Map<String, Object> pop() {
        if(!get().isEmpty()) {
            return get().pop();
        }

        return Collections.emptyMap();
    }

    static void remove() {
        EXPECTED_TYPE.remove();
    }
}
