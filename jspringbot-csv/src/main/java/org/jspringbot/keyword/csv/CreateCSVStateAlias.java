package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Create CSV State Alias", description = "Create an alias name for the current state.", parameters = {"name"})
public class CreateCSVStateAlias extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.createAlias(String.valueOf(params[0]));

        return null;
    }
}
