package org.jspringbot.keyword.office;

import org.jspringbot.Keyword;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class AbstractOfficeKeyword implements Keyword {

    @Autowired
    protected WordOfficeHelper helper;
}
