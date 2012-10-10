package org.jspringbot.keyword.csv;

import org.jspringbot.Keyword;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractCSVKeyword implements Keyword {

    @Autowired
    protected CSVHelper helper;

    public abstract Object execute(Object[] objects) throws Exception;
}
