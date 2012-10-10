package org.jspringbot.keyword.csv.criteria;

import org.apache.commons.collections.CollectionUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Creates a CSV line criteria
 */
public class CSVLineCriteria implements RestrictionAppender {

    private List<Restriction> restrictions;

    private List<String[]> lines;

    private Map<String, Integer> headers;

    public CSVLineCriteria(List<String[]> lines, Map<String, Integer> headers) {
        this.lines = lines;
        this.headers = headers;
        restrictions = new LinkedList<Restriction>();
    }

    public CSVLineCriteria add(Restriction restriction) {
        restrictions.add(restriction);

        return this;
    }

    @Override
    public void append(Restriction r) {
        add(r);
    }

    private boolean matches(String[] line) {
        for(Restriction r : restrictions) {
            if(!r.matches(line, headers)) {
                return false;
            }
        }

        return true;
    }

    public String[] uniqueResult() {
        List<String[]> items = list();

        if(CollectionUtils.isEmpty(items)) {
            return null;
        }

        if(CollectionUtils.size(items) > 1) {
            throw new IllegalStateException("Has more than one entries found.");
        }

        return items.iterator().next();
    }

    public String[] firstResult() {
        LinkedList<String[]> items = list();

        if(CollectionUtils.isEmpty(items)) {
            throw new IllegalStateException("No items found.");
        }

        return items.getFirst();
    }


    public String[] lastResult() {
        LinkedList<String[]> items = list();

        if(CollectionUtils.isEmpty(items)) {
            throw new IllegalStateException("No items found.");
        }

        return items.getLast();
    }

    public LinkedList<String[]> list() {
        final LinkedList<String[]> results = new LinkedList<String[]>();


        for(String[] line : lines) {
            if(matches(line)) {
                results.add(line);
            }
        }

        return results;
    }

    public int count() {
        return CollectionUtils.size(list());
    }
}
