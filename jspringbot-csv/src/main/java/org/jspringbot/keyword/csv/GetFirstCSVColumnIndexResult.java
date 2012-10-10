package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get First CSV Column Index Result", description = "Returns the first result column value for the current criteria query.", parameters = {"index"})
public class GetFirstCSVColumnIndexResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.firstResultColumnIndex(Integer.parseInt(String.valueOf(params[0])));
    }
}
