package org.jspringbot.keyword.csv;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(name = "Should Have CSV Result", description = "Fails if no results was found for the current criteria.")
public class ShouldHaveCSVResult extends AbstractCSVKeyword {

    @Override
    public Object execute(Object[] params) {
        if(helper.projectCount() <= 0) {
            throw new IllegalArgumentException("No results found.");
        }

        return null;
    }
}
