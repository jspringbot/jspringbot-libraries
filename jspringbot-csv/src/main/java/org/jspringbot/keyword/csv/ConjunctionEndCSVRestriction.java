package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Conjunction End CSV Restriction", description = "End of conjunction and restriction for the current criteria.")
public class ConjunctionEndCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.endConjunction();

        return null;
    }
}
