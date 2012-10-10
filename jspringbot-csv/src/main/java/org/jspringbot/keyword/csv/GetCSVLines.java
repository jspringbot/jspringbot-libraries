package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get CSV Lines", description = "Returns a list of string array of the parsed csv.")
public class GetCSVLines extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.getLines();
    }
}
