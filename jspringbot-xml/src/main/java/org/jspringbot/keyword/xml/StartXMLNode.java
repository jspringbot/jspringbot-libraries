package org.jspringbot.keyword.xml;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Start XML Node",
        parameters = {"name", "root=false"},
        description = "classpath:desc/StartXMLNode.txt"
)
public class StartXMLNode extends AbstractXMLKeyword {

    @Override
    public Object execute(Object[] params) {
        String name = String.valueOf(params[0]);
        boolean root = false;

        if(params.length > 1) {
            root = Boolean.parseBoolean(String.valueOf(params[1]));
        }

        try {
            builderHelper.startNode(name, root);
        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Error starting xml node %s.", name));
        }

        return null;
    }
}
