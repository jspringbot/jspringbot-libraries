package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Evaluate Expression Should Be False",
        parameters = {"expression"},
        description = "classpath:desc/EvaluateExpression.txt"
)
public class EvaluateExpressionShouldBeFalse extends AbstractExpressionKeyword {
    @Override
    protected Object executeInternal(Object[] params) throws Exception {
        helper.evaluationShouldBeFalse(String.valueOf(params[0]));

        return null;
    }
}
