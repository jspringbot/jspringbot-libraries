package org.jspringbot.keyword.date.util;

import java.util.Locale;

import org.springframework.context.i18n.LocaleContextHolder;

public class I18nHelper {
	
    public void setLocale(String localeID) {
        
        Locale locale = I18nUtil.getLocaleFromString(localeID);
        LocaleContextHolder.setLocale(locale);

    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }
    
}
