/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.expression;

import de.odysseus.el.TreeValueExpression;
import de.odysseus.el.tree.Node;
import org.jspringbot.keyword.expression.engine.DefaultELContext;
import org.jspringbot.keyword.expression.engine.function.SupportedFunctionsManager;
import org.jspringbot.keyword.expression.plugin.ExpressionHandler;
import org.jspringbot.keyword.expression.plugin.ExpressionHandlerManager;
import org.jspringbot.keyword.expression.plugin.VariableProviderManager;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExpressionHelper implements ApplicationContextAware, ValueEvaluator {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    private static final Pattern EXPRESSION_PATTERN = Pattern.compile("\\$\\[(.*)\\]", Pattern.CASE_INSENSITIVE);

    private static final Pattern PREFIX_EXPRESSION_PATTERN = Pattern.compile("([a-z0-9]+)\\:(.*)", Pattern.CASE_INSENSITIVE);

    private static final Field nodeField;

    static {
        nodeField = ReflectionUtils.findField(TreeValueExpression.class, "node");
        nodeField.setAccessible(true);
    }

    private ExpressionFactory factory;

    private SupportedFunctionsManager functionManager;

    private ExpressionHandlerManager expressionManager;

    private VariableProviderManager variableManager;

    private ELExpressionHandler defaultHandler = new ELExpressionHandler();

    public ExpressionHelper(ExpressionFactory factory) {
        this.factory = factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        functionManager = new SupportedFunctionsManager(context);
        expressionManager = new ExpressionHandlerManager(this, context, defaultHandler);
        variableManager = new VariableProviderManager(context);
    }

    public void evaluationShouldBe(String expression, Object expected) throws Exception {
        LOG.keywordAppender().appendArgument("Expected Result", expected);

        Object value = silentEvaluate(expression);

        if(expected == null && value == null) {
            return;
        }

        if(expected == null || value == null || !silentEvaluate(expected).equals(value)) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not expected.", expression));
        }
    }

    public void evaluationShouldBeNull(String expression) throws Exception {
        Object value = evaluate(expression);

        if(value != null) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' is not null.", expression));
        }
    }

    public void evaluationShouldNotBeNull(String expression) throws Exception {
        Object value = evaluate(expression);

        if(value == null) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' is null.", expression));
        }
    }

    public void evaluationShouldBeTrue(String expression) throws Exception {
        Object value = evaluate(expression);

        if(value == null) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not true.", expression));
        }

        if(!Boolean.class.isInstance(value)) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not true.", expression));
        }

        if(!Boolean.TRUE.equals(value)) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not true.", expression));
        }
    }

    public void evaluationShouldBeFalse(String expression) throws Exception {
        Object value = evaluate(expression);

        if(value == null) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not false.", expression));
        }

        if(!Boolean.class.isInstance(value)) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not false.", expression));
        }

        if(!Boolean.FALSE.equals(value)) {
            throw new IllegalArgumentException(String.format("Evaluation '%s' was not false.", expression));
        }
    }

    public Object silentEvaluate(Object param) throws Exception {
        if(String.class.isInstance(param) && isSupported((String) param)) {
            try {
                LOG.keywordAppender().createPath();

                return evaluate((String) param);
            } finally {
                LOG.keywordAppender().endPath();
            }
        }

        return param;
    }

    private void initVariables(List<Object> variables) throws Exception {
        Map<String, Object> scopedVariables = ScopedVariableHolder.peek();
        int i = 0;
        while(scopedVariables.containsKey(String.format("$%d", i + 1))) {
            i++;
        }
        for (Object variable : variables) {
            ScopedVariableHolder.peek().put(String.format("$%d", i + 1), silentEvaluate(variable));
            i++;
        }
    }

    public Object variableScope(List<Object> variables, Callable<Object> callable) throws Exception {
        try {
            ScopedVariableHolder.push(new HashMap<String, Object>());
            initVariables(variables);

            return callable.call();
        } finally {
            ScopedVariableHolder.pop();
        }
    }

    public void variableScope(List<Object> variables, Runnable runnable) throws Exception {
        try {
            ScopedVariableHolder.push(new HashMap<String, Object>());
            initVariables(variables);

            runnable.run();
        } finally {
            ScopedVariableHolder.pop();
        }
    }

    public Object evaluate(String expression) throws Exception {
        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

        if(!matcher.find()) {
            throw new IllegalArgumentException(String.format("Invalid expression format '%s'.", expression));
        }

        String content = matcher.group(1);

        LOG.keywordAppender().appendExpression(expression);

        Matcher prefixMatcher = PREFIX_EXPRESSION_PATTERN.matcher(content);
        if (prefixMatcher.matches()) {
            String prefix = prefixMatcher.group(1);
            String prefixContent = prefixMatcher.group(2);

            return expressionManager.evaluation(prefix, prefixContent);
        } else {
            return expressionManager.defaultEvaluation(content);
        }
    }

    public boolean isSupported(String expression) {
        Matcher matcher = EXPRESSION_PATTERN.matcher(expression);

        return matcher.matches();
    }

    private Map<String, Object> getVariables() {
        Map<String, Object> variables = new HashMap<String, Object>();

        variables.putAll(ScopedVariableHolder.peek());
        variables.putAll(variableManager.getVariables());

        return variables;
    }

    public Object getValue(Object result) {
        DefaultELContext context = new DefaultELContext(functionManager, getVariables());
        ValueExpression expr = factory.createValueExpression(result, TypeExpressionHolder.peek());

        return expr.getValue(context);
    }

    public class ELExpressionHandler implements ExpressionHandler {

        @Override
        public String getPrefix() {
            throw new UnsupportedOperationException("This is the default handler. This method should not be called.");
        }

        @Override
        public Object evaluate(String expression) throws Exception {
            LOG.keywordAppender().appendProperty("Expression Handler", "Expression Language (JUEL)");

            DefaultELContext context = new DefaultELContext(functionManager, getVariables());
            TreeValueExpression expr = (TreeValueExpression) factory.createValueExpression(context, String.format("${%s}", expression), TypeExpressionHolder.peek());

            Set<String> vars = VariableNodeCapture.capture((Node) nodeField.get(expr));
            for(Map.Entry<String, Object> var : getVariables().entrySet()) {
                if(vars.contains(var.getKey())) {
                    LOG.keywordAppender().appendProperty(String.format("EL Variable ['%s']", var.getKey()), var.getValue());
                }
            }

            Object result = expr.getValue(context);

            LOG.keywordAppender().appendProperty("EL Result", result);

            return result;
        }
    }
}
