package org.jspringbot.keyword.db;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "DB Projected Count Should Be", description = "DB Projected Count Should Be.", parameters = {"count"})
public class DBProjectedCountShouldBe extends AbstractDBKeyword {

    public Object execute(Object[] params) {
        helper.projectedCountShouldBe(Integer.valueOf(String.valueOf(params[0])));

        return null;
    }
}
