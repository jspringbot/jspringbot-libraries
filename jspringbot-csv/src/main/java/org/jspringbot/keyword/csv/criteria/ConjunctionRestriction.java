package org.jspringbot.keyword.csv.criteria;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Will or all provided restrictions
 */
public class ConjunctionRestriction implements Restriction, RestrictionAppender {

    private List<Restriction> restrictions = new LinkedList<Restriction>();



    public ConjunctionRestriction and(Restriction restriction) {
        restrictions.add(restriction);

        return this;
    }


    @Override
    public boolean matches(String[] line, Map<String, Integer> headers) {
        for(Restriction restriction : restrictions) {
            if(!restriction.matches(line, headers)) {
                return false;
            }
        }

        return true;
    }

    @Override
    public void append(Restriction r) {
        and(r);
    }
}
