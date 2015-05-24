package org.jspringbot.keyword.expression;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.argument.ArgumentHandler;
import org.jspringbot.syntax.HighlightRobotLogger;

public class ExpressionArgumentHandler implements ArgumentHandler {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionArgumentHandler.class);

    private ExpressionHelper helper;

    public ExpressionArgumentHandler(ExpressionHelper helper) {
        this.helper = helper;
    }

    @Override
    public boolean isSupported(String keyword, Object parameter) {
        return !StringUtils.startsWith(keyword, "EL Evaluate") &&
                !StringUtils.startsWith(keyword, "EL Should") &&
                String.class.isInstance(parameter) &&
                helper.isSupported((String) parameter);
    }

    @Override
    public Object handle(Object parameter) {
        String expression = (String) parameter;
        try {
            LOG.keywordAppender().createPath();
            return helper.evaluate(expression);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Unable to evaluate expression '%s'.", expression), e);
        } finally {
            LOG.keywordAppender().endPath();
        }
    }
}
