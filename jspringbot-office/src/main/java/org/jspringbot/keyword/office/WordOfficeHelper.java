package org.jspringbot.keyword.office;

import com.aspose.words.*;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class WordOfficeHelper {
    private Document document;

    public void openFile(String path) throws Exception {
        Resource resource = getResource(path);

        document = new Document(resource.getInputStream());
    }

    private Resource getResource(String path) {
        if(!StringUtils.startsWith(path, "file:") && !StringUtils.startsWith(path, "classpath:")) {
            path = "file://" + (!StringUtils.startsWith(path, File.pathSeparator) ? "./" : "") + path;
        }

        ResourceEditor editor = new ResourceEditor();
        editor.setAsText(path);

        return (Resource) editor.getValue();
    }

    public void replaceText(String replaceable, String replacement) throws Exception {
        document.getRange().replace(replaceable, replacement, true, false);
    }

    public void replaceTextAsImage(final String replaceable, String image) throws Exception {
        Resource resource = getResource(image);
        replaceTextAsImage(replaceable, resource.getFile());
    }

    public void replaceTextAsImage(final String replaceable, String image, int width, int height) throws Exception {
        Resource resource = getResource(image);
        replaceTextAsImage(replaceable, resource.getFile(), width, height);
    }

    public void replaceTextAsImage(final String replaceable, File image) throws Exception {
        replaceTextAsImage(replaceable, image, -1, -1);
    }

    @SuppressWarnings("unchecked")
    public void replaceTextAsImage(final String replaceable, File image, int width, int height) throws Exception {
        DocumentBuilder builder = new DocumentBuilder(document);

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

    public File saveAs(String path) throws Exception {
        Resource resource = getResource(path);
        File file = resource.getFile();
        document.save(new FileOutputStream(file), SaveFormat.DOC);

        return file;
    }
}