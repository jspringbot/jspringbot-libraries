package org.jspringbot.syntax;

import org.jspringbot.JSpringBotLogger;
import org.jspringbot.syntax.HighlightRobotLogger.HtmlAppender;

import java.util.Arrays;

public class KeywordAppender {
    private static final JSpringBotLogger LOG = JSpringBotLogger.getLogger(KeywordAppender.class);

    private HtmlAppender appender;

    KeywordAppender(HtmlAppender appender) {
        this.appender = appender;
    }

    public KeywordAppender append(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.append(msg, args);

        return this;
    }

    public KeywordAppender appendBold(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendBold(msg, args);

        return this;
    }

    public KeywordAppender appendPropertyStringArray(String name, String[] values) {
        if(appender == null) {
            LOG.info("%s = %s", name, Arrays.asList(values));
            return this;
        }

        appender.appendPropertyStringArray(name, values);

        return this;
    }

    public KeywordAppender appendProperty(String property, Object value) {
        if(appender == null) {
            LOG.info("%s = %s", property, String.valueOf(value));
            return this;
        }

        appender.appendProperty(property, value);

        return this;
    }

    public KeywordAppender appendText(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendText(msg, args);

        return this;
    }

    public KeywordAppender appendCode(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendCode(msg, args);

        return this;
    }

    public KeywordAppender appendXML(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendXML(msg, args);

        return this;
    }

    public KeywordAppender appendSQL(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendSQL(msg, args);

        return this;
    }

    public KeywordAppender appendJavascript(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendJavascript(msg, args);

        return this;
    }

    public KeywordAppender appendCss(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendCss(msg, args);

        return this;
    }

    public KeywordAppender appendJSON(String msg, Object... args) {
        if(appender == null) {
            LOG.info(msg, args);
            return this;
        }

        appender.appendJSON(msg, args);

        return this;
    }
}
