package org.jspringbot.keyword.office;

import org.junit.Ignore;
import org.junit.Test;

@Ignore
public class WordOfficeHelperTest {

    private WordOfficeHelper helper;

    @Test
    public void testReplace() throws Exception {
        helper = new WordOfficeHelper();

        helper.openFile("classpath:template.doc");
        helper.replaceText("#date", "alvin");
        helper.replaceText("#caseId", "210004146651");

        helper.replaceTextAsImage("#exhibit-a", "classpath:b.png");

        helper.saveAs("/tmp/new.doc");
    }
}