package org.jspringbot.syntax;

import org.apache.commons.io.IOUtils;
import org.jspringbot.Keyword;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractHighlightKeyword implements Keyword {
    @Override
    public Object execute(Object[] params) throws Exception {
        try {
            HighlightKeywordLogger.createAppender(getClass());

            return executeInternal(params);
        } catch (Exception e) {
            HighlightKeywordLogger.appender().appendArguments(params);

            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            try {
                e.printStackTrace(pw);
                pw.flush();
                sw.flush();

                HighlightKeywordLogger.appender().appendCode(sw.toString());
            } finally {
                IOUtils.closeQuietly(pw);
                IOUtils.closeQuietly(sw);
            }

            throw e;
        } finally {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();
        }
    }

    protected abstract Object executeInternal(Object[] params) throws Exception;
}
