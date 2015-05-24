package org.jspringbot.syntax;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jspringbot.lifecycle.LifeCycleAdapter;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

public class KeywordLoggerLifeCycleHandler extends LifeCycleAdapter {
    @Override
    public void startKeyword(String name, Map attributes) {
        HighlightKeywordLogger.createAppender(name);
    }

    @Override
    public void endKeyword(String name, Map attributes) {
        if(HighlightKeywordLogger.appender() != null) {
            if(StringUtils.containsIgnoreCase(name, "JSpringBot")) {
                HighlightKeywordLogger.appender().log();
            }

            HighlightKeywordLogger.clear();
        }
    }

    @Override
    public void endJSpringBotKeyword(String name, Map attributes) {
        if(HighlightKeywordLogger.appender() == null) {
            return;
        }

        if(StringUtils.equals((String) attributes.get("status"), "PASS")) {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();

            return;
        }

        try {
            Object[] params = (Object[]) attributes.get("args");

            HighlightRobotLogger.HtmlAppender appender = HighlightKeywordLogger.appender();
            appender.createPath("Keyword Failure Details:");

            try {
                if (params == null) {
                    appender.appendProperty("Keyword Arguments", null);
                } else if (params.length == 0) {
                    appender.appendProperty("Keyword Arguments", "Array length is 0");
                } else {
                    for (int i = 0; i < params.length; i++) {
                        if (params[i] != null) {
                            appender.appendProperty("Keyword Argument Class [" + i + "]", params[i].getClass().getName());
                        }

                        appender.appendProperty("Keyword Argument Value [" + i + "]", params[i]);
                    }
                }

                if (attributes.containsKey("exception")) {
                    Exception e = (Exception) attributes.get("exception");
                    StringWriter sw = new StringWriter();
                    PrintWriter pw = new PrintWriter(sw);
                    try {
                        e.printStackTrace(pw);
                        pw.flush();
                        sw.flush();

                        appender.appendCode(sw.toString());
                    } finally {
                        IOUtils.closeQuietly(pw);
                        IOUtils.closeQuietly(sw);
                    }
                }
            } finally {
                appender.endPath();
            }
        } finally {
            HighlightKeywordLogger.appender().log();
            HighlightKeywordLogger.clear();
        }
    }
}
