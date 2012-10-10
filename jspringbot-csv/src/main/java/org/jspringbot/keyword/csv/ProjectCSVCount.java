package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Project CSV Count", description = "Returns the projected count of current criteria query.")
public class ProjectCSVCount extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        return helper.projectCount();
    }
}
