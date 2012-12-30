package org.jspringbot.syntax;

import org.jspringbot.JSpringBotLogger;
import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlightRobotLogger.HtmlAppender;
import org.springframework.core.annotation.AnnotationUtils;

class HighlightKeywordLogger {
    public static final ThreadLocal<HtmlAppender> APPENDER_THREAD_LOCAL = new ThreadLocal<HtmlAppender>();

    static void createAppender(Class keywordClass) {
        KeywordInfo keywordInfo = AnnotationUtils.findAnnotation(keywordClass, KeywordInfo.class);

        HtmlAppender appender = new HtmlAppender(JSpringBotLogger.getLogger(keywordClass));
        appender.appendBold(keywordInfo.name());

        APPENDER_THREAD_LOCAL.set(appender);
    }

    static HtmlAppender appender() {
        return APPENDER_THREAD_LOCAL.get();
    }

    static void clear() {
        APPENDER_THREAD_LOCAL.remove();
    }

}
