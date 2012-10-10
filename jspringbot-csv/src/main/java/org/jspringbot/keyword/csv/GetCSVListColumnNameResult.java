package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get CSV List Column Name Result", description = "Returns the column value list for the current criteria query.", parameters = {"name"})
public class GetCSVListColumnNameResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.listColumnName(String.valueOf(params[0]));
    }
}
