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

import org.jspringbot.KeywordInfo;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Run Keyword If",
        parameters = {"condition", "keyword", "*keywordArgs"},
        description = "classpath:desc/ELRunKeywordIf.txt"
)
public class ELRunKeywordIf extends AbstractExpressionKeyword {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    @Override
    public Object execute(final Object[] params) throws Exception {
        if(!Boolean.class.isInstance(params[0])) {
            throw new IllegalArgumentException("Expecting condition with boolean result on first argument.");
        }

        if((Boolean) params[0]) {
            List<Object> items = Arrays.asList(params);

            if(items.size() <= 2) {
                return ELRunKeyword.runKeyword(String.valueOf(params[1]));
            } else {
                return ELRunKeyword.runKeyword(String.valueOf(params[1]), items.subList(2, items.size()));
            }
        }


        return null;
    }
}
