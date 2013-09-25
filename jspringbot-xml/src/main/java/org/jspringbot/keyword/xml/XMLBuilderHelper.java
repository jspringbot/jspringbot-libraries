package org.jspringbot.keyword.xml;

import com.jamesmurty.utils.XMLBuilder;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class XMLBuilderHelper {
    protected XMLBuilder builder;


    public void startNode(String name) throws ParserConfigurationException {
        startNode(name, false);
    }

    public void startNode(String name, boolean rootNode) throws ParserConfigurationException {
        if(rootNode) {
            builder = XMLBuilder.create(name);
        } else {
            if(builder == null) {
                builder = XMLBuilder.create(name);
                return;
            }

            builder = builder.e(name);
        }
    }

    public void endNode() {
        builder = builder.up();
    }

    public void addAttribute(String name, String value) {
        builder = builder.attribute(name, value);
    }

    public void setText(String text) {
        builder = builder.text(text);
    }

    public String asString() throws TransformerException {
        return builder.asString();
    }
}
