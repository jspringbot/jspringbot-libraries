package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Disjunction End CSV Restriction", description = "End of disjunction or restriction for the current criteria.")
public class DisjunctionEndCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.endDisjunction();

        return null;
    }
}
