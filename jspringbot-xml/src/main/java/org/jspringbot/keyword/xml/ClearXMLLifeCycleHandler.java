package org.jspringbot.keyword.xml;

import org.jspringbot.MainContextHolder;
import org.jspringbot.Visitor;
import org.jspringbot.lifecycle.LifeCycleAdapter;
import org.jspringbot.spring.RobotScope;
import org.jspringbot.spring.SpringRobotLibraryManager;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Map;

public class ClearXMLLifeCycleHandler extends LifeCycleAdapter {
    private static final String XML_HELPER_BEAN_NAME = "xmlHelper";
    private static final String XML_BUILDER_HELPER_BEAN_NAME = "xmlBuilderHelper";


    @Override
    public void startTest(String name, Map attributes) {
        if(MainContextHolder.isEnabled()) {
            SpringRobotLibraryManager manager = MainContextHolder.get().getBean(SpringRobotLibraryManager.class);

            manager.visitActive(RobotScope.ALL, new Visitor<ClassPathXmlApplicationContext>() {
                @Override
                public void visit(ClassPathXmlApplicationContext context) {
                    if(context.containsBean(XML_BUILDER_HELPER_BEAN_NAME)) {
                        XMLBuilderHelper helper = (XMLBuilderHelper) context.getBean(XML_BUILDER_HELPER_BEAN_NAME);
                        helper.reset();
                    }

                    if(context.containsBean(XML_HELPER_BEAN_NAME)) {
                        XMLHelper helper = (XMLHelper) context.getBean(XML_HELPER_BEAN_NAME);
                        helper.reset();
                    }
                }
            });
        }
    }
}
