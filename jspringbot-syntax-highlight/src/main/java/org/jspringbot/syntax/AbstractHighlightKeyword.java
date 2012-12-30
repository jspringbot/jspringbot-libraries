package org.jspringbot.syntax;

import org.jspringbot.Keyword;

public abstract class AbstractHighlightKeyword implements Keyword {
    @Override
    public Object execute(Object[] params) throws Exception {
        try {
            HighlightKeywordLogger.createAppender(getClass());

            return executeInternal(params);
        } catch(Exception e) {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();

            throw e;
        }
    }

    protected abstract Object executeInternal(Object[] params) throws Exception;
}
