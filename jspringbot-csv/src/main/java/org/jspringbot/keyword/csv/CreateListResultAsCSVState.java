package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Create List Result As CSV State", description = "Make the current csv list result as the new state csv.", parameters = {"name"})
public class CreateListResultAsCSVState extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        helper.createStateFromList(String.valueOf(params[0]));

        return null;
    }
}
