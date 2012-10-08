package org.jspringbot.syntax;

import org.junit.Test;

/**
 * Test for syntax highlighter
 */
public class HighlighterUtilsTest {
    @Test
    public void testXML() throws Exception {
        System.out.println(HighlighterUtils.INSTANCE.highlight("<a>hello</a>", "xml"));
    }
}
