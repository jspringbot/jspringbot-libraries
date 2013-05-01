package org.jspringbot.function.generator;

import org.jspringbot.keyword.expression.engine.function.SupportedFunctionsManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.io.Resource;

import java.io.IOException;


public class JSONGenerator implements ApplicationContextAware {

    private Resource resource;

    private ApplicationContext applicationContext;

    public JSONGenerator(Resource resource) {
        this.resource = resource;
    }

    public void generate() throws IOException {
        SupportedFunctionsManager functionManager = new SupportedFunctionsManager(applicationContext);

        System.out.println("Document: " + resource.getURI());
        functionManager.generateJSON(resource.getFile());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
