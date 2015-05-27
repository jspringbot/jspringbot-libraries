/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.syntax;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.WordUtils;
import org.jspringbot.JSpringBotLogger;

import java.util.LinkedList;

/**
 * Add highlight support.
 */
public class HighlightRobotLogger extends JSpringBotLogger {

    public static final int WORD_WRAP_LENGTH = 120;

    public static final int TRIM_SIZE = 225;

    public static HighlightRobotLogger getLogger(Class clazz) {
        return new HighlightRobotLogger(clazz);
    }

    protected HighlightRobotLogger(Class logger) {
        super(logger);
    }

    public HtmlAppender createAppender() {
        return new HtmlAppender(this);
    }

    public KeywordAppender keywordAppender() {
        if(HighlightKeywordLogger.appender() == null) {
            return new KeywordAppender(null);
        }

        return new KeywordAppender(HighlightKeywordLogger.appender());
    }

    public static class HtmlAppender {
        private StringBuilder buf = new StringBuilder();

        private StringBuilder header = new StringBuilder();

        private StringBuilder code = new StringBuilder();

        private StringBuilder arguments = new StringBuilder();

        private StringBuilder properties = new StringBuilder();

        private StringBuilder xml = new StringBuilder();

        private StringBuilder sql = new StringBuilder();

        private StringBuilder text = new StringBuilder();

        private StringBuilder json = new StringBuilder();

        private StringBuilder javascript = new StringBuilder();

        private StringBuilder css = new StringBuilder();

        private StringBuilder locator = new StringBuilder();

        private StringBuilder expression = new StringBuilder();

        private JSpringBotLogger logger;

        private LinkedList<String> pathMessages = new LinkedList<String>();

        private LinkedList<HtmlAppender> paths = new LinkedList<HtmlAppender>();

        private boolean silent = false;

        HtmlAppender(JSpringBotLogger logger) {
            this.logger = logger;
        }

        public boolean isSilent() {
            return silent;
        }

        public void setSilent(boolean silent) {
            this.silent = silent;
        }

        public void createPath(String path) {
            HtmlAppender pathAppender = new HtmlAppender(logger);
            pathAppender.setSilent(silent);

            if(StringUtils.isNotBlank(path)) {
                pathAppender.appendHeader(path);
            }

            paths.push(pathAppender);
        }

        public void endPath() {
            HtmlAppender pathAppender = paths.removeLast();

            String message = pathAppender.buildLog();
            if(StringUtils.isNotBlank(message)) {
                pathMessages.add(message);
            }
        }

        public HtmlAppender append(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().append(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                buf.append(String.format(msg, args));
            } else {
                buf.append(msg);
            }

            return this;
        }

        public HtmlAppender appendHeader(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendHeader(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            header.append("<b>");
            if(args != null && args.length > 0) {
                header.append(String.format(msg, args));
            } else {
                header.append(msg);
            }

            header.append("</b>");

            return this;
        }

        public HtmlAppender appendBold(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendBold(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            buf.append("<b>");
            append(msg, args);
            buf.append("</b>");

            return this;
        }

        public HtmlAppender appendPropertyStringArray(String name, String[] values) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendPropertyStringArray(name, values);
            }

            if(isSilent()) {
                return this;
            }

            for(int i = 0; i < values.length; i++) {
                appendProperty(name + "[" + i + "]", values[i]);
            }

            return this;
        }

        private String hardWordWrap(String str) {
            if(HighlighterUtils.instance().isEnable()) {
                return str;
            } else {
                return WordUtils.wrap(str, WORD_WRAP_LENGTH, "\n    ", true);
            }
        }

        public HtmlAppender appendArgument(String property, Object value) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendArgument(property, value);
            }

            if(isSilent()) {
                return this;
            }

            return append(arguments, property, value);
        }

        public HtmlAppender appendFullArgument(String property, Object value) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendArgument(property, value);
            }

            if(isSilent()) {
                return this;
            }

            return append(arguments, property, value, false);
        }

        public HtmlAppender appendArgumentComment(String comment) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendArgumentComment(comment);
            }

            return appendComment(properties, comment);
        }

        public HtmlAppender appendPropertyComment(String comment) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendPropertyComment(comment);
            }

            return appendComment(properties, comment);
        }

        public HtmlAppender appendProperty(String property, Object value) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendProperty(property, value);
            }

            if(isSilent()) {
                return this;
            }

            return append(properties, property, value);
        }

        public HtmlAppender appendFullProperty(String property, Object value) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendProperty(property, value);
            }

            if(isSilent()) {
                return this;
            }

            return append(properties, property, value, false);
        }


        private HtmlAppender appendComment(StringBuilder properties, String comment) {
            if(properties.length() > 0) {
                properties.append("\n");
            }

            properties.append(comment);

            return this;
        }

        private HtmlAppender append(StringBuilder properties, String property, Object value) {
            return append(properties, property, value, true);
        }

        private HtmlAppender append(StringBuilder properties, String property, Object value, boolean trim) {
            if(properties.length() > 0) {
                properties.append("\n");
            }

            if(Number.class.isInstance(value)) {
                if(Long.class.isInstance(value) || Integer.class.isInstance(value) || Byte.class.isInstance(value) || Short.class.isInstance(value)) {
                    properties.append(hardWordWrap(String.format("%s = %d", property, ((Number) value).longValue())));
                } else {
                    properties.append(hardWordWrap(String.format("%s = %f", property, ((Number) value).doubleValue())));
                }
            } else if(Boolean.class.isInstance(value)) {
                properties.append(hardWordWrap(String.format("%s = %s", property, String.valueOf(value))));
            } else if(Character.class.isInstance(value)) {
                properties.append(hardWordWrap(String.format("%s = '%s'", property, String.valueOf(value))));
            } else if(value == null) {
                properties.append(String.format("%s = null", property));
            } else if(Object[].class.isInstance(value)) {
                StringBuilder buf = new StringBuilder();

                int i =0;
                for(Object o : (Object[]) value) {
                    if(buf.length() > 0) {
                        buf.append(", ");
                    }
                    buf.append("[").append(i++).append("] ").append("\"").append(StringEscapeUtils.escapeJava(String.valueOf(o))).append("\"");
                }

                String strValue = buf.toString();
                if(trim && strValue.length() > TRIM_SIZE) {
                    strValue = StringUtils.substring(buf.toString(), 0, TRIM_SIZE) + "...";
                }

                properties.append(hardWordWrap(String.format("%s = (%s) %s", property, ((Object[]) value).getClass().getSimpleName(), strValue)));
            } else {
                String strValue = String.valueOf(value);
                if(trim && strValue.length() > TRIM_SIZE) {
                    strValue = StringUtils.substring(buf.toString(), 0, TRIM_SIZE) + "...";
                }

                properties.append(hardWordWrap(String.format("%s = \"%s\"", property, StringEscapeUtils.escapeJava(strValue))));
            }

            return this;
        }

        public HtmlAppender appendText(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendText(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                text.append(String.format(msg, args));
            } else {
                text.append(msg);
            }

            return this;
        }

        public HtmlAppender appendCode(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendCode(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                code.append(String.format(msg, args));
            } else {
                code.append(msg);
            }

            return this;
        }

        public HtmlAppender appendXML(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendXML(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                xml.append(String.format(msg, args));
            } else {
                xml.append(msg);
            }

            return this;
        }

        public HtmlAppender appendSQL(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendSQL(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                sql.append(String.format(msg, args));
            } else {
                sql.append(msg);
            }

            return this;
        }

        public HtmlAppender appendJavascript(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendJavascript(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                javascript.append(String.format(msg, args));
            } else {
                javascript.append(msg);
            }

            return this;
        }

        public HtmlAppender appendCss(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendCss(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                css.append(String.format(msg, args));
            } else {
                css.append(msg);
            }

            return this;
        }

        public HtmlAppender appendLocator(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendLocator(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(locator.length() > 0) {
                locator.append("\n");
            }

            if(args != null && args.length > 0) {
                locator.append(String.format(msg, args));
            } else {
                locator.append(msg);
            }

            return this;
        }

        public HtmlAppender appendExpression(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendExpression(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(expression.length() > 0) {
                // only log one expression, the main expression
                return this;
            }

            if(args != null && args.length > 0) {
                expression.append(String.format(msg, args));
            } else {
                expression.append(msg);
            }

            return this;
        }

        public HtmlAppender appendJSON(String msg, Object... args) {
            if(CollectionUtils.isNotEmpty(paths)) {
                return paths.peekLast().appendJSON(msg, args);
            }

            if(isSilent()) {
                return this;
            }

            if(args != null && args.length > 0) {
                json.append(String.format(msg, args));
            } else {
                json.append(msg);
            }

            return this;
        }

        private String buildLog() {
            if(isSilent()) {
                return null;
            }

            if(locator.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightLocator(locator.toString()));
            }

            if(arguments.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightText(arguments.toString()));
            }

            if(expression.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightExpression(expression.toString()));
            }

            if(properties.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightText(properties.toString()));
            }

            if(text.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightText(text.toString()));
            }

            if(xml.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightXML(xml.toString()));
            }

            if(sql.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightSQL(sql.toString()));
            }

            if(javascript.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightJavascript(javascript.toString()));
            }

            if(css.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightCss(css.toString()));
            }

            if(json.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightJSON(json.toString()));
            }

            if(code.length() > 0) {
                buf.append(HighlighterUtils.INSTANCE.highlightNormal(code.toString()));
            }

            if(buf.length() > 0) {
                buf.insert(0, header.toString());
            }

            // log path messages
            for(String message : pathMessages) {
                buf.append(message);
            }

            return buf.toString();
        }

        public void log() {
            if(isSilent()) {
                return;
            }

            String message = buildLog();
            if(StringUtils.isNotBlank(message)) {
                logger.pureHtml(message);
            }
        }
    }
}
