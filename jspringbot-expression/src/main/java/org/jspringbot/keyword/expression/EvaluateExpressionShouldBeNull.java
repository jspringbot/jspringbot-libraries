package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Evaluate Expression Should Be Null",
        parameters = {"expression"},
        description = "classpath:desc/EvaluateExpression.txt"
)
public class EvaluateExpressionShouldBeNull extends AbstractExpressionKeyword {
    @Override
    protected Object executeInternal(Object[] params) throws Exception {
        helper.evaluationShouldBeNull(String.valueOf(params[0]));

        return null;
    }
}
