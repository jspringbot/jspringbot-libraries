package org.jspringbot.keyword.i18n;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.Locale;

public class I18nObject {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(I18nHelper.class);

    private MessageSourceAccessor messages;

    public I18nObject(MessageSourceAccessor messages) {
        this.messages = messages;
    }

    public String get(String code) {
        Locale locale = LocaleContextHolder.getLocale();
        LOG.keywordAppender()
                .appendProperty("Message Code", code)
                .appendProperty("Locale ID", locale.toString());

        if(StringUtils.isNotBlank(locale.getDisplayCountry())) {
            LOG.keywordAppender().appendProperty("Display Country", locale.getDisplayCountry());
        }
        if(StringUtils.isNotBlank(locale.getDisplayLanguage())) {
            LOG.keywordAppender().appendProperty("Display Language", locale.getDisplayLanguage());
        }

        String message = messages.getMessage(code);

        LOG.keywordAppender().appendProperty("Message Value", message);

        return message;
    }

    public String getDisplayLanguage() {
        Locale locale = LocaleContextHolder.getLocale();

        LOG.keywordAppender().appendProperty("Display Language", locale.getDisplayLanguage());

        return locale.getDisplayLanguage();
    }

    public String getDisplayCountry() {
        Locale locale = LocaleContextHolder.getLocale();

        LOG.keywordAppender().appendProperty("Display Country", locale.getDisplayCountry());

        return locale.getDisplayCountry();
    }

    public String getLanguage() {
        Locale locale = LocaleContextHolder.getLocale();

        LOG.keywordAppender().appendProperty("Locale Language", locale.getLanguage());

        return locale.getDisplayLanguage();
    }

    public String getCountry() {
        Locale locale = LocaleContextHolder.getLocale();

        LOG.keywordAppender().appendProperty("Locale Country", locale.getCountry());

        return locale.getDisplayCountry();
    }
}
