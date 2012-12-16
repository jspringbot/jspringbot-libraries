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

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.io.IOException;
import java.util.Locale;

public class I18nHelper {

    private MessageSourceAccessor messages;

    public I18nHelper(MessageSource messageSource) throws IOException {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setLanguage(String localeString) {
        Locale locale = I18nUtil.getLocaleFromString(localeString);
        LocaleContextHolder.setLocale(locale);
    }

    public String getMessage(String code) {
        return messages.getMessage(code);
    }

    public I18nDictionary createDictionary() {
        return new I18nDictionary(messages);
    }
}
