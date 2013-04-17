package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Run Keyword Expect Error",
        parameters = {"keyword", "*keywordArgs"},
        description = "classpath:desc/ELRunKeywordExpectError.txt")
public class ELRunKeywordExpectError extends AbstractExpressionKeyword {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    @Override
    public Object execute(final Object[] params) throws Exception {
        List<Object> items = Arrays.asList(params);

        try {
            String keyword = String.valueOf(params[0]);
            if(items.size() <= 1) {
                ELRunKeyword.runKeyword(keyword);
            } else {
                ELRunKeyword.runKeyword(keyword, items.subList(1, items.size()));
            }

            throw new IllegalStateException("Keyword '%s' expected an error but did not happen.");
        } catch(Exception e) {
            return null;
        }
    }
}
