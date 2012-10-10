package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Parse CSV Resource", description = "Parse the given CSV resource.", parameters = {"csvResourcePath"})
public class ParseCSVResource extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.parseCSVResource(String.valueOf(params[0]));

        return null;
    }
}
