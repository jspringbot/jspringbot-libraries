package org.jspringbot.keyword.office;

import org.junit.Ignore;
import org.junit.Test;

import java.util.Date;

@Ignore
public class WordOfficeHelperTest {

    private WordHelper helper;

    @Test
    public void testReplace() throws Exception {
        helper = new WordHelper();

        helper.openFile("classpath:template.doc");
        helper.replaceText("@date", new Date().toString());
        helper.replaceText("@caseId", "210004146651");

        helper.replaceTextAsImage("@exhibit1", "classpath:b.png", 400, 200);

        helper.saveAs("/tmp/new.docx", "DOCX");
    }

    @Test
    public void testCreate() throws Exception {
        helper = new WordHelper();

        helper.create();
        helper.insertText("Sample text.");
        helper.paragraphBreak();
        helper.insertImage("classpath:b.png", 400, 200);
        helper.paragraphBreak();
        helper.pageBreak();
        helper.insertHyperlink("jspringbot", "http://jspringbot.org");
        helper.saveAs("/tmp/created.doc");
    }
}