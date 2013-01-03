package org.jspringbot.keyword.xml;

import org.apache.commons.collections.CollectionUtils;
import org.jspringbot.keyword.expression.ExpressionHelper;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.w3c.dom.Element;

import javax.xml.transform.TransformerException;
import java.util.List;

public class XMLUtils {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    private static XMLHelper getHelper() {
        return ApplicationContextHolder.get().getBean(XMLHelper.class);
    }

    public static String attr(Object... args) throws TransformerException {
        if(Element.class.isInstance(args[0])) {
            Element element = (Element) args[0];
            String attribute = (String) args[1];
            String result = element.getAttribute(attribute);

            LOG.keywordAppender()
                    .appendProperty("Attribute", attribute)
                    .appendProperty("Result", result);

            return result;
        } else if(String.class.isInstance(args[0]) && args.length > 1) {
            String xpath = (String) args[0];
            String attribute = (String) args[1];

            LOG.keywordAppender()
                    .appendProperty("Xpath", xpath)
                    .appendProperty("Attribute", attribute);

            List<Element> els = elements(xpath);

            if(CollectionUtils.isEmpty(els)) {
                throw new IllegalArgumentException(String.format("No elements found for xpath '%s'.", xpath));
            }

            String result = els.iterator().next().getAttribute(attribute);

            LOG.keywordAppender().appendProperty("Result", result);

            return result;
        } else if(String.class.isInstance(args[0])) {
            String attribute = (String) args[0];

            LOG.keywordAppender().appendProperty("Root Attribute", attribute);

            String result = getHelper().getDocument().getDocumentElement().getAttribute(attribute);

            LOG.keywordAppender().appendProperty("Result", result);

            return result;
        }

        throw new IllegalArgumentException("Expected 'xml:attr(attrName)' or 'xml:attr(element, attrName)' or 'xml:attr(xpath, attrName)'.");
    }

    public static List<Element> elements(String xpath) throws TransformerException {
        return getHelper().getXpathElements(xpath);
    }
}
