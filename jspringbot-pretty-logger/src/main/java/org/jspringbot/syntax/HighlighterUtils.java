/*
 * Copyright (c) 2015. JSpringBot. All Rights Reserved.
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
import syntaxhighlight.ParseResult;
import syntaxhighlight.Style;
import syntaxhighlight.Theme;
import syntaxhighlighter.SyntaxHighlighterParser;
import syntaxhighlighter.brush.*;
import syntaxhighlighter.theme.*;

import java.awt.*;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.List;

/**
 * Highlighter utility class
 */
public class HighlighterUtils {

    public static final String DEFAULT_THEME = "default";

    public static final Map<String, Theme> THEME_MAP = new HashMap<String, Theme>() {{
        put(DEFAULT_THEME, new ThemeDefault());
        put("django", new ThemeDjango());
        put("eclipse", new ThemeEclipse());
        put("emacs", new ThemeEmacs());
        put("grey", new ThemeFadeToGrey());
        put("mdultra", new ThemeMDUltra());
        put("midnight", new ThemeMidnight());
        put("rdark", new ThemeRDark());
    }};


    public static final HighlighterUtils INSTANCE = new HighlighterUtils();

    public static HighlighterUtils instance() {
        return INSTANCE;
    }

    private boolean enable = false;

    private Theme theme;

    private Map<String, SyntaxHighlighterParser> parserMap;

    private HighlighterUtils() {
        parserMap = new HashMap<String, SyntaxHighlighterParser>();

        parserMap.put("xml", new SyntaxHighlighterParser(new BrushXml()));
        parserMap.put("css", new SyntaxHighlighterParser(new BrushCss()));
        parserMap.put("json", new SyntaxHighlighterParser(new BrushJScript()));
        parserMap.put("javascript", new SyntaxHighlighterParser(new BrushJScript()));
        parserMap.put("sql", new SyntaxHighlighterParser(new BrushSql()));
        parserMap.put("text", new SyntaxHighlighterParser(new BrushPlain()));
        parserMap.put("clojure", new SyntaxHighlighterParser(new BrushJScript()));

        theme = THEME_MAP.get(DEFAULT_THEME);
    }

    public void setTheme(String theme) {
        this.theme = THEME_MAP.get(theme);

        if(this.theme == null) {
            this.theme = THEME_MAP.get(DEFAULT_THEME);
        }
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String highlightProperties(Properties properties, String comment) {
        try {
            StringWriter writer = new StringWriter();
            properties.store(writer, comment);
            return highlight(writer.toString(), "properties");
        } catch (IOException e) {
            return properties.toString();
        }
    }

    public String highlightNormal(String code) {
        return highlight(code, "text");
    }

    public String highlightText(String code) {
        return highlight(code, "javascript");
    }

    public String highlightXML(String code) {
        return highlight(code, "xml", true);
    }

    public String highlightJSON(String code) {
        return highlight(code, "json", true);
    }

    public String highlightJavascript(String code) {
        return highlight(code, "javascript", true);
    }

    public String highlightCss(String code) {
        return highlight(code, "css", true);
    }

    public String highlightSQL(String code) {
        return highlight(code, "sql", true);
    }

    public String highlight(String code, String type) {
        return highlight(code, type, false);
    }

    public String highlight(String code, String type, boolean linenumber) {
        if(!enable) {
            return "\n" + StringEscapeUtils.escapeHtml(code);
        }

        SyntaxHighlighterParser parser = parserMap.get(type);
        if(parser == null) {
            return "\n" + StringEscapeUtils.escapeHtml(code);
        }

        int i = 0;
        StringBuilder buf = new StringBuilder("<pre style=\"padding:3px;");

        if(theme.getBackground() != null) {
            cssColor(buf, "background-color", theme.getBackground());
        }
        if(theme.getFont() != null) {
            buf.append("font-family:").append(theme.getFont().getFamily()).append(";");
        }

        buf.append("\">");

        for (ParseResult result : parser.parse(code)) {
            if(i > result.getOffset()) continue;

            String before = StringUtils.substring(code, i, result.getOffset());
            if (!StringUtils.isEmpty(before)) {
                style(buf, "plain", StringEscapeUtils.escapeHtml(before));
            }

            String token = StringUtils.substring(code, result.getOffset(), result.getOffset() + result.getLength());
            style(buf, result.getStyleKeys(), token);

            i = result.getOffset() + result.getLength();
        }

        String before = StringUtils.substring(code, i, i + code.length());
        if (!StringUtils.isEmpty(before)) {
            style(buf, "plain", StringEscapeUtils.escapeHtml(before));
        }

        return buf.append("</pre>").toString();
    }

    private void style(StringBuilder buf, String style, String token) {
        style(buf, Collections.singletonList(style), token);
    }

    private void style(StringBuilder buf, List<String> styles, String token) {
        buf.append("<span");

        if(CollectionUtils.isNotEmpty(styles)) {
            buf.append(" style=\"");

            for(String styleKey : styles) {
                Style style = theme.getStyle(styleKey);

                if(style.isBold()) {
                    buf.append("font-weight:bold;");
                }
                if(style.isItalic()) {
                    buf.append("font-style:italic;");
                }
                if(style.isUnderline()) {
                    buf.append("text-decoration:underline;");
                }

                Color foreColor = style.getColor();
                Color bgColor = style.getBackground();

                if(foreColor != null) {
                    cssColor(buf, "color", foreColor);
                }
                if(bgColor != null) {
                    cssColor(buf, "background-color", bgColor);
                }
            }

            buf.append("\"");
        }

        buf.append(">");
        buf.append(token);
        buf.append("</span>");
    }

    private static void cssColor(StringBuilder buf, String style, Color color) {
        String rgb = Integer.toHexString(color.getRGB());
        rgb = rgb.substring(2, rgb.length());

        buf.append(style).append(":#").append(StringUtils.lowerCase(rgb)).append(";");
    }
}
