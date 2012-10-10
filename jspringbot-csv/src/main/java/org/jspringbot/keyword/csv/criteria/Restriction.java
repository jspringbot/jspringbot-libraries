package org.jspringbot.keyword.csv.criteria;

import java.util.Map;

public interface Restriction {

    boolean matches(String[] line, Map<String, Integer> headers);
}
