package org.jspringbot.keyword.expression;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-expression.xml"})
public class ExpressionHelperTest {

    @Autowired
    private EvaluateExpression evaluator;

    @Autowired
    private EvaluateExpressionShouldBeTrue trueEvaluator;


    private Object evaluate(String expression) throws Exception {
        return evaluator.execute(new Object[] {expression});
    }

    private void evaluateAsTrue(String expression) throws Exception {
        trueEvaluator.execute(new Object[] {expression});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedFormat() throws Exception {
        evaluate("unsupported");
    }

    @Test
    public void testEvaluate() throws Exception {
        assertEquals(2L, evaluate("#{1+1}"));
        evaluateAsTrue("#{contains('alvin', 'vin')}");
    }
}
