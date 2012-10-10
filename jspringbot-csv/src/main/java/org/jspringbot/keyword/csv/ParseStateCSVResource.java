package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Parse State CSV Resource", description = "Parse the given CSV resource with the given state name.", parameters = {"name", "csvResourcePath"})
public class ParseStateCSVResource extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.parseCSVResource(String.valueOf(params[0]), String.valueOf(params[1]));

        return null;
    }
}
