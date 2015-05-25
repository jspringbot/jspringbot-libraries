package org.jspringbot.keyword.office;

import com.aspose.words.*;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.xwpf.usermodel.BodyElementType;
import org.apache.poi.xwpf.usermodel.IBodyElement;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jsoup.helper.Validate;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.util.ReflectionUtils;

import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.jspringbot.keyword.expression.utils.ELFileUtils.resource;
import static org.jspringbot.keyword.expression.utils.ELFileUtils.stream;

import java.util.List;

public class WordHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(WordHelper.class);

    private Document document;

    private DocumentBuilder builder;

    private File openedFile;

    public void create() throws Exception {
        document = new Document();
        builder = new DocumentBuilder(document);
    }

    public void openFile(String path) throws Exception {
        LOG.createAppender()
                .appendBold("Open File:")
                .appendProperty("path", path)
                .log();

        openedFile = resource(path);
        document = new Document(stream(openedFile));
        builder = new DocumentBuilder(document);
    }

    public void insertText(String text) throws Exception {
        LOG.createAppender()
                .appendBold("Insert Text:")
                .appendCss(text)
                .log();

        builder.write(text);
    }

    public void paragraphBreak() throws Exception {
        builder.writeln();
    }

    public void pageBreak() throws Exception {
        builder.writeln();
        builder.insertBreak(BreakType.PAGE_BREAK);
    }

    public void insertHtml(String html) throws Exception {
        LOG.createAppender()
                .appendBold("Insert Html:")
                .appendCss(html)
                .log();

        builder.insertHtml(html);
    }

    public void insertHyperlink(String display, String url) throws Exception {
        LOG.createAppender()
                .appendBold("Insert Hyperlink:")
                .appendProperty("display", display)
                .appendProperty("path", url)
                .log();

        builder.getFont().setColor(Color.BLUE);
        builder.getFont().setUnderline(Underline.SINGLE);
        builder.insertHyperlink(display, url, false);
        builder.getFont().clearFormatting();
    }

    public void replaceText(String replaceable, String replacement) throws Exception {
        LOG.createAppender()
                .appendBold("Replace Text:")
                .appendProperty("replaceable", replaceable)
                .appendProperty("replacement", replacement)
                .log();

        document.getRange().replace(replaceable, replacement, true, false);
    }

    public void replaceTextAsImage(final String replaceable, String image) throws Exception {
        replaceTextAsImage(replaceable, resource(image));
    }

    public void replaceTextAsImage(final String replaceable, String image, int width, int height) throws Exception {
        replaceTextAsImage(replaceable, resource(image), width, height);
    }

    public void replaceTextAsImage(final String replaceable, File image) throws Exception {
        replaceTextAsImage(replaceable, image, -1, -1);
    }

    @SuppressWarnings("unchecked")
    public void replaceTextAsImage(final String replaceable, File image, int width, int height) throws Exception {
        LOG.createAppender()
                .appendBold("Replace Text As Image:")
                .appendProperty("replaceable", replaceable)
                .appendProperty("image", image.getAbsolutePath())
                .appendProperty("width", width)
                .appendProperty("height", height)
                .log();

        final List<Paragraph> found = new ArrayList<Paragraph>(5);
        document.accept(new DocumentVisitor() {
            @Override
            public int visitParagraphStart(Paragraph paragraph) throws Exception {
                if(paragraph.getRange().getText().contains(replaceable)) {
                    found.add(paragraph);
                }

                return 0;
            }
        });

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(image));

        for(Paragraph paragraph : found) {
            builder.moveTo(paragraph);

            if(width > 0 && height > 0) {
                builder.insertImage(bytes, width, height);
            } else {
                builder.insertImage(bytes);
            }
        }

        replaceText(replaceable, "");
    }

    public void insertImage(String image) throws Exception {
        insertImage(resource(image));
    }

    public void insertImage(String image, int width, int height) throws Exception {
        insertImage(resource(image), width, height);
    }

    public void insertImage(File image) throws Exception {
        insertImage(image, -1, -1);
    }

    @SuppressWarnings("unchecked")
    public void insertImage(File image, int width, int height) throws Exception {
        LOG.createAppender()
                .appendBold("Insert Image:")
                .appendProperty("image", image.getAbsolutePath())
                .appendProperty("width", width)
                .appendProperty("height", height)
                .log();

        byte[] bytes = IOUtils.toByteArray(new FileInputStream(image));
        if(width > 0 && height > 0) {
            builder.insertImage(bytes, width, height);
        } else {
            builder.insertImage(bytes);
        }
    }

    public File save() throws Exception {
        return save(null);
    }

    public File save(String format) throws Exception {
        Validate.notNull(openedFile, "no opened file.");

        return save(openedFile, format);
    }

    public File saveAs(String path) throws Exception {
        return saveAs(path, null);
    }

    public File saveAs(String path, String format) throws Exception {
        File file = resource(path);

        return save(file, format);
    }

    public File save(File file, String format) throws Exception {
        LOG.createAppender()
                .appendBold("Save:")
                .appendProperty("file", file.getAbsolutePath())
                .appendProperty("format", format)
                .log();

        int savedFormat;

        if(format == null) {
            savedFormat = document.getOriginalLoadFormat();
        } else {
            Field field = ReflectionUtils.findField(SaveFormat.class, format);
            savedFormat = (Integer) field.get(SaveFormat.class);
        }

        document.save(new FileOutputStream(file), savedFormat);

        // remove the first paragraph
        // this is the evaluation message of the aspose generated document
        switch (savedFormat) {
            case SaveFormat.DOC: removeDocFirstParagraph(file); break;
            case SaveFormat.DOCX: removeDocxFirstParagraph(file); break;
        }

        LOG.html("<a href='file://%s'/>Download File...</a>", file.getAbsolutePath());

        return file;
    }

    // remove first paragraph of the document
    private void removeDocFirstParagraph(File file) throws IOException {
        HWPFDocument document1 = new HWPFDocument(new FileInputStream(file));
        document1.getOverallRange().getParagraph(0).delete();

        document1.write(new FileOutputStream(file));
    }

    // remove first paragraph of the document
    private void removeDocxFirstParagraph(File file) throws IOException {
        XWPFDocument document1 = new XWPFDocument(new FileInputStream(file));

        List<IBodyElement> bodyElements = document1.getBodyElements();
        for(int i = 0; i < bodyElements.size(); i++) {
            IBodyElement element = bodyElements.get(i);

            if(element.getElementType() == BodyElementType.PARAGRAPH) {
                document1.removeBodyElement(i);
                break;
            }
        }

        document1.write(new FileOutputStream(file));
    }
}