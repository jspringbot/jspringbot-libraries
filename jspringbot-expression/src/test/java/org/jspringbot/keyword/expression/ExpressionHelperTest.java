package org.jspringbot.keyword.expression;

import junitx.util.PrivateAccessor;
import org.apache.commons.lang.StringEscapeUtils;
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
    private ELEvaluate evaluator;

    @Autowired
    private ELShouldBeTrue trueEvaluator;

    @Autowired
    private ELShouldBeFalse falseEvaluator;

    @Autowired
    private ELShouldBeEqual equalEvaluator;

    @Autowired
    private ELAddVariable addVariable;

    @Autowired
    private ApplicationContext context;


    private Object evaluate(Object... values) throws Exception {
        return evaluator.execute(values);
    }

    private void evaluateEquals(Object... values) throws Exception {
        equalEvaluator.execute(values);
    }

    private void addVariable(String name, Object value) throws Exception {
        addVariable.execute(new Object[] {name, value});
    }

    private void evaluateAsTrue(Object... values) throws Exception {
        trueEvaluator.execute(values);
    }

    private void evaluateAsFalse(Object... values) throws Exception {
        falseEvaluator.execute(values);
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
        evaluateEquals("#{i:math:abs(-1)}", 1);

        List<String> items = new ArrayList<String>();

        addVariable("items", items);
        evaluateAsTrue("#{col:isEmpty(items)}");
        evaluateAsFalse("#{col:isNotEmpty(items)}");

        evaluateAsTrue("#{col:isEmpty($1)}", items);
        evaluateAsFalse("#{col:isNotEmpty($1)}", items);

        items.add("test");

        evaluateAsTrue("#{col:isNotEmpty(items)}");
        evaluateAsTrue("#{col:isNotEmpty($1)}", items);
        evaluateEquals("#{col:size(items)}", 1);

        items.add("test2");

        evaluateEquals("#{col:size(items)}", 2);

    }

    @Test
    public void testEval() throws Exception {
        evaluateEquals("#{l:eval('#{i:100}')}", 100l);

        List<String> items = new ArrayList<String>();
        items.add("test");

        evaluateEquals("#{f:eval('#{col:size($1)}')}", 1.0f, items);
        evaluateEquals("#{eval('#{$2 - $3}', 6, 5) eq col:size($1)}", true, items);
    }

    @Test
    public void testVArgsAndMethod() throws Exception {
        evaluateEquals("#{concat('1', '2', '3')}", "123");
        evaluateEquals("#{join('-', '1', '2', '3')}", "1-2-3");
        evaluateEquals("#{substring('alvin', 2)}", "vin");
        evaluateEquals("#{substring('alvin', 2, 4)}", "vi");
        evaluateEquals("#{'alvin'.length()}", 5);
        evaluateEquals("#{b:concat('tr', 'ue')}", true);
    }

    @Test
    public void testNesting() throws Exception {
        evaluateEquals("#{i:l:f:d:5.6}", 5);
    }

    @Test
    public void testCreateStringFunctionsXML() throws Exception {
        new ClassStaticFunctionsPrinter(StringEscapeUtils.class).addPrefix("escape:").print(System.out);
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
