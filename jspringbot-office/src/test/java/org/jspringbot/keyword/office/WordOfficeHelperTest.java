package org.jspringbot.keyword.office;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

@Ignore
public class WordOfficeHelperTest {

    private WordOfficeHelper helper;

    @Test
    public void testReplace() throws Exception {
        helper = new WordOfficeHelper();

        helper.openFile("/Users/adeleon/Downloads/a.doc");
        helper.replaceText("@date", new Date().toString());
        helper.replaceText("@caseId", "210004146651");

        helper.replaceTextAsImage("@exhibit-a", "classpath:b.png", 400, 200);

        helper.saveAs("/tmp/new.doc");
    }
}