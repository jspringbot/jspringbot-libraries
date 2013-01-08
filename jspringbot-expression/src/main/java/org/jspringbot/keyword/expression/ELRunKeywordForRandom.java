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

package org.jspringbot.keyword.expression;

import org.apache.commons.collections.CollectionUtils;
import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Run Keyword For Random",
        parameters = {"keyword", "randomCount", "itemName", "itemList"},
        description = "classpath:desc/ELRunKeywordForRandom.txt"
)
public class ELRunKeywordForRandom extends AbstractExpressionKeyword {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    @Override
    public Object execute(final Object[] params) throws Exception {
        if(!List.class.isInstance(params[3])) {
            throw new IllegalArgumentException("Expecting list for first argument.");
        }

        int random = Integer.parseInt(String.valueOf(params[1]));
        List items = (List) params[3];

        if(CollectionUtils.isEmpty(items)) {
            return null;
        }

        List<Integer> indices = new ArrayList<Integer>(items.size());
        for(int i = 0; i < items.size(); i++) {
            indices.add(i);
        }

        Collections.shuffle(indices);
        for(int i = 0; i < indices.size() && i < random; i++) {
            LOG.keywordAppender().appendProperty("Random index", indices.get(i));

            Object item = items.get(indices.get(i));
            defaultVariableProvider.add("randomIndex", indices.get(i));
            defaultVariableProvider.add(String.valueOf(params[2]), item);
            ELRunKeyword.runKeyword(String.valueOf(params[0]));
        }

        return null;
    }
}
