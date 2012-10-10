package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Set CSV Headers", description = "Sets the csv header.", parameters = {"csvHeaderString"})
public class SetCSVHeaders extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.setHeaders(String.valueOf(params[0]));

        return null;
    }
}
