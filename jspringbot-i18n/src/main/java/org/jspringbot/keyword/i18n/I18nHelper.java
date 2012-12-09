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

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.MessageSourceAccessor;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class I18nHelper implements BeanFactoryAware {

    private MessageSourceAccessor messages;

    private BeanFactory factory;

    public I18nHelper(MessageSource messageSource) {
        this.messages = new MessageSourceAccessor(messageSource);
    }

    public void setLanguage(String localeString) {
        Locale locale = I18nUtil.getLocaleFromString(localeString);
        LocaleContextHolder.setLocale(locale);
    }

    public String getMessage(String code) {
        return messages.getMessage(code);
    }

    @SuppressWarnings("unchecked")
    public Map<String, String> createDictionary(String bean) {
        List<String> codes = (List<String>) factory.getBean(bean);

        Map<String, String> dictionary = new HashMap<String, String>(codes.size());

        for(String code : codes) {
            dictionary.put(code, getMessage(code));
        }

        return dictionary;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.factory = beanFactory;
    }
}
