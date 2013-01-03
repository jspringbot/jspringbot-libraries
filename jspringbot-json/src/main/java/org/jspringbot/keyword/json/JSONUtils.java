package org.jspringbot.keyword.json;

import net.minidev.json.JSONObject;
import org.apache.commons.collections.CollectionUtils;
import org.jspringbot.spring.ApplicationContextHolder;
import org.jspringbot.syntax.HighlightRobotLogger;

import javax.xml.transform.TransformerException;
import java.util.List;

public class JSONUtils {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(JSONUtils.class);

    private static JSONHelper getHelper() {
        return ApplicationContextHolder.get().getBean(JSONHelper.class);
    }

    public static List objects(String jsonPath) throws TransformerException {
        return getHelper().getJsonValues(jsonPath);
    }

    public static Object singleProperty(JSONObject obj, String propertyName) {
        LOG.keywordAppender().appendProperty("JSON Property Name", propertyName);

        Object propertyValue = obj.get(propertyName);

        LOG.keywordAppender().appendProperty("JSON Property Value", propertyValue);

        return propertyValue;
    }

    public static Object property(Object... args) throws TransformerException {
        if(JSONObject.class.isInstance(args[0]) && args.length == 2) {
            JSONObject obj = (JSONObject) args[0];
            String property = (String) args[1];

            return singleProperty(obj, property);
        } else if(String.class.isInstance(args[0]) && args.length == 2) {
            String jsonPath = (String) args[0];
            String property = (String) args[1];

            LOG.keywordAppender().appendProperty("JSON Path", jsonPath);

            List items = objects(jsonPath);

            if(CollectionUtils.isEmpty(items)) {
                throw new IllegalArgumentException(String.format("No objects found for jsonPath '%s'", jsonPath));
            }

            JSONObject obj = (JSONObject) items.iterator().next();
            return singleProperty(obj, property);
        } else if(String.class.isInstance(args[0]) && args.length == 1) {
            String jsonPath = (String) args[0];

            LOG.keywordAppender().appendProperty("JSON Path", jsonPath);

            return getHelper().getJsonValue(jsonPath);
        }

        return null;
    }

}
