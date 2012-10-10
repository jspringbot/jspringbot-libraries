package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Project CSV Count Should Be", description = "Fails if the projected count of current criteria query does not match the given expected count.", parameters = {"expectedCount"})
public class ProjectCSVCountShouldBe extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        int count = helper.projectCount();

        int expectedCount = Integer.parseInt(String.valueOf(params[0]));
        if(count != expectedCount) {
            throw new IllegalArgumentException(String.format("Expecting query projected count '%d' but was '%d'", expectedCount, count));
        }

        return null;
    }
}
