package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@KeywordInfo(name = "Create CSV Criteria", description = "Creates a new csv criteria, will override the existing criteria if there is one.")
public class CreateCSVCriteria extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) throws IOException {
        helper.createCriteria();

        return null;
    }
}
