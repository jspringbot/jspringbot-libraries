package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Parse CSV String", description = "Parse the given CSV string.", parameters = {"csvString"})
public class ParseCSVString extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.parseCSVString(String.valueOf(params[0]));

        return null;
    }
}
