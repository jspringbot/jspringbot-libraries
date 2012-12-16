package org.jspringbot.keyword.i18n;

import org.springframework.context.support.MessageSourceAccessor;

public class I18nDictionary {
    private MessageSourceAccessor messages;

    public I18nDictionary(MessageSourceAccessor messages) {
        this.messages = messages;
    }

    public String get(String code) {
        return messages.getMessage(code);
    }
}
