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

import org.python.core.PySystemState;
import org.python.util.PythonInterpreter;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Properties;

/**
 * Highlighter utility class
 */
public class HighlighterUtils {

    public static final HighlighterUtils INSTANCE = new HighlighterUtils();

    private PythonInterpreter interpreter;

    private HighlighterUtils() {
        interpreter = new PythonInterpreter(null, new PySystemState());
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
        return highlight(code, "clojure");
    }

    public String highlightXML(String code) {
        return highlight(code, "xml", true);
    }

    public String highlightJSON(String code) {
        return highlight(code, "json", true);
    }

    public String highlightSQL(String code) {
        return highlight(code, "sql", true);
    }

    public String highlight(String code, String type) {
        return highlight(code, type, false);
    }

    public String highlight(String code, String type, boolean linenumber) {
        interpreter.set("code", code);
        interpreter.set("type", type);

        interpreter.exec("from pygments import highlight\n" +
                "from pygments.lexers import get_lexer_by_name\n" +
                "from pygments.formatters import HtmlFormatter\n" +
                "formatter = HtmlFormatter(cssclass=\"syntax\"" + (linenumber ? ",linenos=\"table\"" : "") + ")\n" +
                "result = highlight(code, get_lexer_by_name(type), formatter)\n");

        return String.valueOf(interpreter.get("result")) + "<link rel='stylesheet' href='highlight.css'>";
    }
}
