package org.jspringbot.syntax;

import org.jspringbot.JSpringBotLogger;
import org.jspringbot.Keyword;

public abstract class AbstractHighlightKeyword implements Keyword {
    @Override
    public Object execute(Object[] params) throws Exception {
        try {
            HighlightKeywordLogger.createAppender(JSpringBotLogger.getLogger(getClass()));

            return executeInternal();
        } catch(Exception e) {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();

            throw e;
        }
    }

    protected abstract Object executeInternal() throws Exception;
}
