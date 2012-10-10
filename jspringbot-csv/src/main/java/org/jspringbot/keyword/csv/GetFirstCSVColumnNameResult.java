package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get First CSV Column Name Result", description = "Returns the first result column value of the current criteria query.", parameters = {"name"})
public class GetFirstCSVColumnNameResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.firstResultColumnName(String.valueOf(params[0]));
    }
}
