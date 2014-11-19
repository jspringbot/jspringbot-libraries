package org.jspringbot.keyword.json;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.CharEncoding;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:spring-test.xml"})
public class JSONPathTest {
    @Autowired
    private JSONHelper helper;

    private String getTestJson() throws IOException {
        ResourceEditor editor = new ResourceEditor();
        editor.setAsText("classpath:test.json");

        Resource resource = (Resource) editor.getValue();

        return IOUtils.toString(resource.getInputStream(), CharEncoding.UTF_8);
    }

    @Test
    public void testSample() throws Exception {
        helper.setJsonString(getTestJson());

        List<Object> nowShowing = helper.getJsonValues("movies[?(@.showing == false)]");
        assertThat(nowShowing.size(), is(2));
    }
}
