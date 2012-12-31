package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Evaluate Expression Should Be",
        parameters = {"expression", "expected"},
        description = "classpath:desc/EvaluateExpression.txt"
)
public class EvaluateExpressionShouldBe extends AbstractExpressionKeyword {
    @Override
    protected Object executeInternal(Object[] params) throws Exception {
        helper.evaluationShouldBe(String.valueOf(params[0]), params[1]);

        return null;
    }
}
