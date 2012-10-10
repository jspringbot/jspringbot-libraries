package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Conjunction Start CSV Restriction", description = "The next restrictions added will be as and conjunction restrictions.")
public class ConjunctionStartCSVRestriction extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.startConjunction();

        return null;
    }
}
