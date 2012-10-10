package org.jspringbot.keyword.csv.criteria;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Will or all provided restrictions
 */
public class DisjunctionRestriction implements Restriction, RestrictionAppender {

    private List<Restriction> restrictions = new LinkedList<Restriction>();

    public DisjunctionRestriction or(Restriction restriction) {
        restrictions.add(restriction);

        return this;
    }


    @Override
    public boolean matches(String[] line, Map<String, Integer> headers) {
        for(Restriction restriction : restrictions) {
            if(restriction.matches(line, headers)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public void append(Restriction r) {
        or(r);
    }
}
