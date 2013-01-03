package org.jspringbot.keyword.expression;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Should Not Be Null",
        parameters = {"expression", "*variables"},
        description = "classpath:desc/ELShouldNotBeNull.txt"
)
public class ELShouldNotBeNull extends AbstractExpressionKeyword {
    @Override
    public Object execute(final Object[] params) throws Exception {
        List<Object> variables = new ArrayList<Object>();

        if (params.length > 1) {
            variables.addAll(Arrays.asList(params).subList(1, params.length));
        }

        helper.variableScope(variables, new Runnable() {
            @Override
            public void run() {
                try {
                    helper.evaluationShouldNotBeNull(String.valueOf(params[0]));
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
