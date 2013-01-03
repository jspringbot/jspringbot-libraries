package org.jspringbot.keyword.i18n;

import org.jspringbot.KeywordInfo;
import org.springframework.stereotype.Component;

@Component
@KeywordInfo(
        name = "Create i18n Object",
        description = "classpath:desc/CreateI18nObject.txt"
)
public class CreateI18nObject extends Abstracti18nKeyword {

    @Override
    public Object execute(Object[] params) {
        return i18nHelper.createI18nObject();
    }

}
