// Copyright (c) 2011 Chan Wai Shing
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package syntaxhighlighter.brush;

import java.util.ArrayList;
import java.util.List;

/**
 * Locator brush.
 */
public class BrushLocator extends Brush {

    public BrushLocator() {
        super();

        String tags = "a abbr acronym address applet area article aside audio b base basefont bdi bdo big blockquote body " +
                "br button canvas caption center cite code col colgroup datalist dd del details dfn dialog dir div dl dt em " +
                "embed fieldset figcaption figure font footer form frame frameset h1  to h6 head header hr html i iframe img " +
                "input ins kbd keygen label legend li link main map mark menu menuitem meta meter nav noframes noscript object " +
                "ol optgroup option output p param pre progress q rp rt ruby s samp script section select small source span " +
                "strike strong style sub summary sup table tbody td textarea tfoot th thead time title tr track tt u ul var video wbr";

        List<RegExpRule> _regExpRuleList = new ArrayList<RegExpRule>();
        _regExpRuleList.add(new RegExpRule(RegExpRule.multiLineCComments, "comments")); // multiline comments
        _regExpRuleList.add(new RegExpRule(RegExpRule.doubleQuotedString, "string")); // double quoted strings
        _regExpRuleList.add(new RegExpRule(RegExpRule.singleQuotedString, "string")); // single quoted strings
        _regExpRuleList.add(new RegExpRule("\\.[a-zA-Z0-9_\\-]+", "variable")); // html colors
        _regExpRuleList.add(new RegExpRule("\\#[a-zA-Z0-9_\\-]+", "functions")); // html colors
        _regExpRuleList.add(new RegExpRule("\\@[a-zA-Z0-9_\\-]+", "color1"));
        _regExpRuleList.add(new RegExpRule("id=", "keyword"));
        _regExpRuleList.add(new RegExpRule("name=", "keyword"));
        _regExpRuleList.add(new RegExpRule("text=", "keyword"));
        _regExpRuleList.add(new RegExpRule("partial=", "keyword"));
        _regExpRuleList.add(new RegExpRule("css=", "keyword"));
        _regExpRuleList.add(new RegExpRule("xpath=", "keyword"));
        _regExpRuleList.add(new RegExpRule("dom=", "keyword"));
        _regExpRuleList.add(new RegExpRule("link=", "keyword"));
        _regExpRuleList.add(new RegExpRule("tag=", "keyword"));
        _regExpRuleList.add(new RegExpRule(getKeywords(tags), "constants")); // functions
        setRegExpRuleList(_regExpRuleList);

        setHTMLScriptRegExp(new HTMLScriptRegExp("(?:&lt;|<)\\s*style.*?(?:&gt;|>)", "(?:&lt;|<)\\/\\s*style\\s*(?:&gt;|>)"));
    }

}
