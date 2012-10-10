package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Add Column Name Equals CSV Restriction", description = "Add a column name equals restriction.", parameters = {"name", "value"})
public class AddColumnNameEqualsCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.addColumnNameEqualsRestriction(String.valueOf(params[0]), String.valueOf(params[1]));

        return null;
    }
}
