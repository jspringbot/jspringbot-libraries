package org.jspringbot.syntax;

import org.jspringbot.Keyword;

public abstract class AbstractHighlightKeyword implements Keyword {
    @Override
    public Object execute(Object[] params) throws Exception {
        try {
            HighlightKeywordLogger.createAppender(getClass());

            return executeInternal(params);
        } finally {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();
        }
    }

    protected abstract Object executeInternal(Object[] params) throws Exception;
}
