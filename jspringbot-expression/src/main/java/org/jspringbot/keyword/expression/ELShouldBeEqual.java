package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Should Be Equal",
        parameters = {"expression", "expected", "*variables"},
        description = "classpath:desc/ELEvaluate.txt"
)
public class ELShouldBeEqual extends AbstractExpressionKeyword {
    @Override
    protected Object executeInternal(final Object[] params) throws Exception {
        List<Object> variables = new ArrayList<Object>();

        if (params.length > 2) {
            variables.addAll(Arrays.asList(params).subList(2, params.length));
        }

        helper.variableScope(variables, new Runnable() {
            @Override
            public void run() {
                try {
                    helper.evaluationShouldBe(String.valueOf(params[0]), params[1]);
                } catch (RuntimeException e) {
                    throw e;
                } catch (Exception e) {
                    throw new IllegalStateException(e.getMessage(), e);
                }
            }
        });

        return null;
    }
}
