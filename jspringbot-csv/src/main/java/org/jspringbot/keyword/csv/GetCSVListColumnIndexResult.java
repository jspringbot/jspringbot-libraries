package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get CSV List Column Index Result", description = "Returns the column value list for the current criteria query.", parameters = {"index"})
public class GetCSVListColumnIndexResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.listColumnIndex(Integer.parseInt(String.valueOf(params[0])));
    }
}
