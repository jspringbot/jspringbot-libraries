/*
 * Copyright (c) 2012. JSpringBot. All Rights Reserved.
 *
 * See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The JSpringBot licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.jspringbot.keyword.i18n;

import org.apache.commons.lang.StringUtils;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.util.Locale;

public class I18nHelper {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(I18nHelper.class);

    private MessageSourceAccessor messages;

    public I18nHelper(MessageSource messageSource) throws IOException {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setLocale(String localeID) {
        LOG.keywordAppender().appendProperty("Locale ID", localeID);

        Locale locale = I18nUtil.getLocaleFromString(localeID);
        LocaleContextHolder.setLocale(locale);

        if(StringUtils.isNotBlank(locale.getDisplayCountry())) {
            LOG.keywordAppender().appendProperty("Display Country", locale.getDisplayCountry());
        }
        if(StringUtils.isNotBlank(locale.getDisplayLanguage())) {
            LOG.keywordAppender().appendProperty("Display Language", locale.getDisplayLanguage());
        }
    }

    public Locale getLocale() {
        return LocaleContextHolder.getLocale();
    }

    public String getMessage(String code) {
        Locale locale = getLocale();
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
        Locale locale = getLocale();

        LOG.keywordAppender().appendProperty("Display Language", locale.getDisplayLanguage());

        return locale.getDisplayLanguage();
    }

    public String getDisplayCountry() {
        Locale locale = getLocale();

        LOG.keywordAppender().appendProperty("Display Country", locale.getDisplayCountry());

        return locale.getDisplayCountry();
    }

    public String getLanguage() {
        Locale locale = getLocale();

        LOG.keywordAppender().appendProperty("Locale Language", locale.getLanguage());

        return locale.getLanguage();
    }

    public String getCountry() {
        Locale locale = getLocale();

        LOG.keywordAppender().appendProperty("Locale Country", locale.getCountry());

        return locale.getCountry();
    }

    public I18nObject createI18nObject() {
        return new I18nObject(messages);
    }

    public String getMessage(String localeID, String messageCode) {
        Locale old = getLocale();

        try {
            Locale locale = I18nUtil.getLocaleFromString(localeID);
            LocaleContextHolder.setLocale(locale);

            return getMessage(messageCode);
        } finally {
            LocaleContextHolder.setLocale(old);
        }
    }
}
