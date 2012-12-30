package org.jspringbot.syntax;

import org.jspringbot.syntax.HighlightRobotLogger.HtmlAppender;

public class KeywordAppender {
    private HtmlAppender appender;

    KeywordAppender(HtmlAppender appender) {
        this.appender = appender;
    }

    public KeywordAppender append(String msg, Object... args) {
        appender.append(msg, args);

        return this;
    }

    public KeywordAppender appendBold(String msg, Object... args) {
        appender.appendBold(msg, args);

        return this;
    }

    public KeywordAppender appendPropertyStringArray(String name, String[] values) {
        appender.appendPropertyStringArray(name, values);

        return this;
    }

    public KeywordAppender appendProperty(String property, Object value) {
        appender.appendProperty(property, value);

        return this;
    }

    public KeywordAppender appendText(String msg, Object... args) {
        appender.appendText(msg, args);

        return this;
    }

    public KeywordAppender appendCode(String msg, Object... args) {
        appender.appendCode(msg, args);

        return this;
    }

    public KeywordAppender appendXML(String msg, Object... args) {
        appender.appendXML(msg, args);

        return this;
    }

    public KeywordAppender appendSQL(String msg, Object... args) {
        appender.appendSQL(msg, args);

        return this;
    }

    public KeywordAppender appendJavascript(String msg, Object... args) {
        appender.appendJavascript(msg, args);

        return this;
    }

    public KeywordAppender appendCss(String msg, Object... args) {
        appender.appendCss(msg, args);

        return this;
    }

    public KeywordAppender appendJSON(String msg, Object... args) {
        appender.appendJSON(msg, args);

        return this;
    }
}
