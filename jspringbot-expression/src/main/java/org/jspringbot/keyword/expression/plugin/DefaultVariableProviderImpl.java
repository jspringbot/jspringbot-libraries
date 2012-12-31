package org.jspringbot.keyword.expression.plugin;

import org.jspringbot.syntax.HighlightRobotLogger;

import java.util.HashMap;
import java.util.Map;

public class DefaultVariableProviderImpl implements VariableProvider {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(DefaultVariableProviderImpl.class);

    private Map<String, Object> variables = new HashMap<String, Object>();

    @Override
    public Map<String, Object> getVariables() {
        return variables;
    }

    public void add(String key, Object value) {
        LOG.keywordAppender()
                .appendProperty("Variable Key", key)
                .appendProperty("Variable Value", value);

        variables.put(key, value);
    }

    public void clear() {
        for(Map.Entry<String, Object> entry : variables.entrySet()) {
            LOG.keywordAppender().appendProperty(String.format("Variable [%s]", entry.getKey()), entry.getValue());
        }

        variables.clear();
    }
}
