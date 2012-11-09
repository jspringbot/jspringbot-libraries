package org.jspringbot.keyword.db;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Projected Count Should Be", description = "Projected Count Should Be.", parameters = {"count"})
public class ProjectedCountShouldBe extends AbstractDBKeyword {

    public Object execute(Object[] params) {
        helper.projectedCountShouldBe(Integer.valueOf(String.valueOf(params[0])));

        return null;
    }
}
