package org.jspringbot.keyword.db;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static junit.framework.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class DbHelperTest {

    @Autowired
    private DbHelper helper;

    @Before
    public void setUp() throws Exception {
        helper.begin();
    }

    @After
    public void tearDown() throws Exception {
        helper.rollback();
    }

    @Test
    public void testAddQueryAndDelete() throws Exception {
        helper.createQueryByName("insert.account");
        helper.setStringParameter("userName", "userName");
        helper.setStringParameter("name", "account1");
        helper.setStringParameter("description", "");

        assertEquals(1, helper.executeUpdate());

        // projected count should be 1
        helper.createQueryByName("get.account.size");
        helper.executeQuery();
        helper.projectedCountShouldBeEqual(1);

        // insert account 2
        helper.createQueryByName("insert.account");
        helper.setStringParameter("userName", "userName");
        helper.setStringParameter("name", "account2");
        helper.setStringParameter("description", "");

        assertEquals(1, helper.executeUpdate());

        helper.createQueryByName("delete.accounts");
        assertEquals(2, helper.executeUpdate());
    }
}
