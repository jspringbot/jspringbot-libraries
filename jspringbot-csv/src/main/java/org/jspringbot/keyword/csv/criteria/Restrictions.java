package org.jspringbot.keyword.csv.criteria;

/**
 * All valid restrictions
 */
public class Restrictions {
    public static DisjunctionRestriction disjunction() {
        return new DisjunctionRestriction();
    }

    public static ConjunctionRestriction conjunction() {
        return new ConjunctionRestriction();
    }

    public static Restriction columnNameEquals(String name, String value) {
        return new ColumnEqualsRestriction(name, value);
    }

    public static Restriction columnIndexEquals(int index, String value) {
        return new ColumnEqualsRestriction(index, value);
    }
}
