package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Disjunction Start CSV Restriction", description = "The next restrictions added will be as or disjunction restrictions.")
public class DisjunctionStartCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.startDisjunction();

        return null;
    }
}
