package org.jspringbot.keyword.http;

import org.jspringbot.lifecycle.LifeCycleAdapter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Map;

public class HTTPLifeCycleHandler extends LifeCycleAdapter implements ApplicationContextAware {

    private HTTPHelper helper;

    private boolean newSessionOnStartSuite = false;

    private boolean newSessionOnStartTest = false;

    public void setNewSessionOnStartSuite(boolean newSessionOnStartSuite) {
        this.newSessionOnStartSuite = newSessionOnStartSuite;
    }

    public void setNewSessionOnStartTest(boolean newSessionOnStartTest) {
        this.newSessionOnStartTest = newSessionOnStartTest;
    }

    @Override
    public void startSuite(String name, Map attributes) {
        if(newSessionOnStartSuite) {
            helper.newSession();
        }
    }

    @Override
    public void startTest(String name, Map attributes) {
        if(newSessionOnStartTest) {
            helper.newSession();
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        helper = applicationContext.getBean(HTTPHelper.class);
    }
}
