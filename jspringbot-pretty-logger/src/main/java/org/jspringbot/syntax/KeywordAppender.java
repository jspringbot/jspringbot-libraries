package org.jspringbot.syntax;

import org.jspringbot.syntax.HighlightRobotLogger.HtmlAppender;

public class KeywordAppender {
    private HtmlAppender appender;

    KeywordAppender(HtmlAppender appender) {
        this.appender = appender;
    }

    public boolean isSilent() {
        return appender.isSilent();
    }

    public void setSilent(boolean silent) {
        appender.setSilent(silent);
    }

    public KeywordAppender createPath() {
        if(appender == null) {
            return this;
        }

        appender.createPath("");
        return this;
    }

    public KeywordAppender createPath(String path) {
        if(appender == null) {
            return this;
        }

        appender.createPath(path);
        return this;
    }

    public KeywordAppender endPath() {
        if(appender == null) {
            return this;
        }

        appender.endPath();
        return this;
    }

    public KeywordAppender append(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.append(msg, args);

        return this;
    }

    public KeywordAppender appendArgumentComment(String comment) {
        if(appender == null) {
            return this;
        }

        appender.appendArgumentComment(comment);

        return this;
    }

    public KeywordAppender appendPropertyComment(String comment) {
        if(appender == null) {
            return this;
        }

        appender.appendPropertyComment(comment);

        return this;
    }

    public KeywordAppender appendBold(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendBold(msg, args);

        return this;
    }

    public KeywordAppender appendLocator(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendLocator(msg, args);

        return this;
    }

    public KeywordAppender appendExpression(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendExpression(msg, args);

        return this;
    }

    public KeywordAppender appendPropertyStringArray(String name, String[] values) {
        if(appender == null) {
            return this;
        }

        appender.appendPropertyStringArray(name, values);

        return this;
    }

    public KeywordAppender appendArgument(String property, Object value) {
        if(appender == null) {
            return this;
        }

        appender.appendArgument(property, value);

        return this;
    }

    public KeywordAppender appendProperty(String property, Object value) {
        if(appender == null) {
            return this;
        }

        appender.appendProperty(property, value);

        return this;
    }

    public KeywordAppender appendText(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendText(msg, args);

        return this;
    }

    public KeywordAppender appendCode(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendCode(msg, args);

        return this;
    }

    public KeywordAppender appendXML(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendXML(msg, args);

        return this;
    }

    public KeywordAppender appendSQL(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendSQL(msg, args);

        return this;
    }

    public KeywordAppender appendJavascript(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendJavascript(msg, args);

        return this;
    }

    public KeywordAppender appendCss(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendCss(msg, args);

        return this;
    }

    public KeywordAppender appendJSON(String msg, Object... args) {
        if(appender == null) {
            return this;
        }

        appender.appendJSON(msg, args);

        return this;
    }
}
