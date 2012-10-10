package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Parse State CSV String", description = "Parse the given CSV string with the given state name.", parameters = {"name", "csvString"})
public class ParseStateCSVString extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.parseCSVString(String.valueOf(params[0]), String.valueOf(params[1]));

        return null;
    }
}
