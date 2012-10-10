package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get CSV List Result", description = "Returns the list of string array of the current criteria query.")
public class GetCSVListResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.list();
    }
}
