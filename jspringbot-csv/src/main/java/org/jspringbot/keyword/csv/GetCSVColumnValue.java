package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Get CSV Column Value", description = "Retrieves the column value given the column name.", parameters = {"line", "columnName"})
public class GetCSVColumnValue  extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        String[] line = (String[]) params[0];
        String columnName = String.valueOf(params[1]);

        return helper.getColumnValue(line, columnName);
    }
}

