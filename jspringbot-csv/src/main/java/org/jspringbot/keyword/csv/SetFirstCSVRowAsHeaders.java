package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Set First CSV Row As Headers", description = "Set first CSV row as the headers.")
public class SetFirstCSVRowAsHeaders extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.setFirstLineAsHeader();

        return null;
    }
}
