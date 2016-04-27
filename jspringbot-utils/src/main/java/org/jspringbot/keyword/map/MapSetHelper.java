package org.jspringbot.keyword.map;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.JSpringBotLogger;

import java.util.*;


public class MapSetHelper {

    private static final JSpringBotLogger LOGGER = JSpringBotLogger.getLogger(MapSetHelper.class);

    private Map<String, Set<String>> items;

    public MapSetHelper() {
        this.items = new LinkedHashMap<String, Set<String>>();
    }

    public void reset() {
        items.clear();
    }

    public boolean add(String key, String value) {
        Set<String> values = items.get(key);

        if(values == null) {
            values = new LinkedHashSet<String>();
            items.put(key, values);
        }

        return values.add(value);
    }

    public String randomConcat(String key, String delimiter, int size) {
        if(!items.containsKey(key)) {
            throw new IllegalArgumentException("No values for key " + key);
        }

        Set<String> values = items.get(key);

        if(size > values.size()) {
            LOGGER.info("size reduced to actual item size: from " + size + " to " + values.size());
            size = values.size();
        }

        List<String> shuffled = new ArrayList<String>(values);

        Collections.shuffle(shuffled);

        return StringUtils.join(shuffled.subList(0, size), delimiter);
    }

    public Set<String> getKeys() {
        if(items.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }

        return items.keySet();

    }

    public String getRandomKey() {
        if(items.isEmpty()) {
            throw new IllegalArgumentException("Map is empty");
        }

        List<String> shuffled = new ArrayList<String>(items.keySet());
        Collections.shuffle(shuffled);

        return shuffled.iterator().next();
    }

    public int size(String key) {
        if(!items.containsKey(key)) {
            throw new IllegalArgumentException("No values for key " + key);
        }

        return items.get(key).size();
    }
}
