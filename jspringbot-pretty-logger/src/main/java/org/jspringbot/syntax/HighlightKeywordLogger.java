package org.jspringbot.syntax;

import org.jspringbot.JSpringBotLogger;
import org.jspringbot.syntax.HighlightRobotLogger.HtmlAppender;

class HighlightKeywordLogger {
    public static final ThreadLocal<HtmlAppender> APPENDER_THREAD_LOCAL = new ThreadLocal<HtmlAppender>();

    static void createAppender(String keyword) {
        HtmlAppender appender = new HtmlAppender(JSpringBotLogger.getLogger(HighlightKeywordLogger.class));
        appender.appendBold(keyword);

        APPENDER_THREAD_LOCAL.set(appender);
    }

    static HtmlAppender appender() {
        return APPENDER_THREAD_LOCAL.get();
    }

    static void clear() {
        APPENDER_THREAD_LOCAL.remove();
    }
}
