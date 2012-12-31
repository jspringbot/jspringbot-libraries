package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Evaluate Expression Should Not Be Null",
        parameters = {"expression"},
        description = "classpath:desc/EvaluateExpression.txt"
)
public class EvaluateExpressionShouldNotBeNull extends AbstractExpressionKeyword {
    @Override
    protected Object executeInternal(Object[] params) throws Exception {
        helper.evaluationShouldNotBeNull(String.valueOf(params[0]));

        return null;
    }
}
