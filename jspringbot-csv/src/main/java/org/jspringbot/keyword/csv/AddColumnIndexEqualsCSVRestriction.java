package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Add Column Index Equals CSV Restriction", description = "Add a column index equals restriction.", parameters = {"index", "value"})
public class AddColumnIndexEqualsCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.addColumnIndexEqualsRestriction(Integer.parseInt(String.valueOf(params[0])), String.valueOf(params[1]));

        return null;
    }
}
