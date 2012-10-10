package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Switch CSV State", description = "Switch csv state given the state name.", parameters = {"name"})
public class SwitchCSVState extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.switchState(String.valueOf(params[0]));

        return null;
    }
}
