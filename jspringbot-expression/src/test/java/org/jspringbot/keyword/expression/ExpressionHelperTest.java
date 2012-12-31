package org.jspringbot.keyword.expression;

import junitx.util.PrivateAccessor;
import org.apache.commons.collections.MapUtils;
import org.jspringbot.spring.ApplicationContextHolder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring/spring-expression.xml"})
public class ExpressionHelperTest {

    @Autowired
    private EvaluateExpression evaluator;

    @Autowired
    private EvaluateExpressionShouldBeTrue trueEvaluator;

    @Autowired
    private EvaluateExpressionShouldBeFalse falseEvaluator;

    @Autowired
    private EvaluateExpressionShouldBe equalEvaluator;

    @Autowired
    private AddExpressionVariable addVariable;


    @Autowired
    private ApplicationContext context;


    private Object evaluate(String expression) throws Exception {
        return evaluator.execute(new Object[] {expression});
    }

    private void evaluateEquals(String expression, Object value) throws Exception {
        equalEvaluator.execute(new Object[] {expression, value});
    }

    private void addVariable(String name, Object value) throws Exception {
        addVariable.execute(new Object[] {name, value});
    }

    private void evaluateAsTrue(String expression) throws Exception {
        trueEvaluator.execute(new Object[] {expression});
    }

    private void evaluateAsFalse(String expression) throws Exception {
        falseEvaluator.execute(new Object[] {expression});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUnsupportedFormat() throws Exception {
        evaluate("unsupported");
    }

    @Test
    public void testEvaluate() throws Exception {
        assertEquals(2L, evaluate("#{1+1}"));
        evaluateAsTrue("#{contains('alvin', 'vin')}");
        evaluateEquals("#{100}", "#{100}");
        evaluateEquals("#{i:'100'}", 100);
        evaluateEquals("#{l:'100'}", 100l);
        evaluateEquals("#{d:'100'}", 100.0);
        evaluateEquals("#{f:'100'}", 100.0f);
        evaluateEquals("#{s:100}", "100");
        evaluateEquals("#{b:'false'}", false);
        evaluateEquals("#{l:eval('#{i:100}')}", 100l);
        evaluateEquals("#{i:math:abs(-1)}", 1);

        List<String> items = new ArrayList<String>();

        addVariable("items", items);
        evaluateAsTrue("#{col:isEmpty(items)}");
        evaluateAsFalse("#{col:isNotEmpty(items)}");

        items.add("test");

        evaluateAsTrue("#{col:isNotEmpty(items)}");
        evaluateEquals("#{col:size(items)}", 1);

        items.add("test2");

        evaluateEquals("#{col:size(items)}", 2);
    }

    @Test
    public void testCreateStringFunctionsXML() throws Exception {
        new ClassStaticFunctionsPrinter(MapUtils.class).addPrefix("map").print(System.out);
    }

    @Before
    public void setUp() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "set", new Class[] {ApplicationContext.class}, new Object[] {context});
    }

    @After
    public void tearDown() throws Throwable {
        PrivateAccessor.invoke(ApplicationContextHolder.class, "remove", new Class[] {}, new Object[] {});
    }
}
