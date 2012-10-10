package org.jspringbot.keyword.csv.criteria;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.util.Map;

/**
 * Add a column equals restriction
 */
public class ColumnEqualsRestriction implements Restriction {

    private Integer index;

    private String name;

    private String value;

    public ColumnEqualsRestriction(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public ColumnEqualsRestriction(Integer index, String value) {
        this.index = index;
        this.value = value;
    }

    @Override
    public boolean matches(String[] line, Map<String, Integer> headers) {
        Validate.isTrue((MapUtils.isNotEmpty(headers) && name != null && headers.containsKey(name)) || index != null, "index or name is invalid.");

        if(name != null) {
            index = headers.get(name);
        }

        assert index != null;

        return StringUtils.equals(line[index], value);
    }
}
