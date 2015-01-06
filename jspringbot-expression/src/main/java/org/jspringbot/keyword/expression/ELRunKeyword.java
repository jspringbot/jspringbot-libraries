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
import org.jspringbot.MainContextHolder;
import org.jspringbot.PythonUtils;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.python.util.PythonInterpreter;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
@KeywordInfo(
        name = "EL Run Keyword",
        parameters = {"keyword", "*arguments"},
        description = "classpath:desc/ELRunKeyword.txt"
)
public class ELRunKeyword extends AbstractExpressionKeyword {
    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(ExpressionHelper.class);

    private static Object doPythonExec(String keyword, List<Object> params) {
    	PythonInterpreter interpreter = MainContextHolder.get().getBean(PythonInterpreter.class);
    	
    	interpreter.set("keyword", keyword);
    	interpreter.set("args", params);

    	
        interpreter.exec(
                "from robot.libraries.BuiltIn import BuiltIn\n" +
                        "result= BuiltIn().run_keyword(keyword, *args)\n"
        );

        Object result = interpreter.get("result");
    	
        if(result != null) {
            LOG.keywordAppender().appendProperty(String.format("runKeyword('%s')", keyword), result.getClass());
        } else {
            LOG.keywordAppender().appendProperty(String.format("runKeyword('%s')", keyword), null);
            return null;
        }

        Object javaObject = PythonUtils.toJava(result);

        if(javaObject != null) {
            LOG.keywordAppender().appendProperty(String.format("runKeyword('%s')", keyword), javaObject.getClass());
        }

        LOG.keywordAppender().appendProperty(String.format("runKeyword('%s')", keyword), javaObject);

        return javaObject;
    }
    
    public static Object runKeyword(String keyword, List<Object> params) {
    	
    	return doPythonExec(keyword, params);
    	
    }
    
    
    public static Object runKeyword(String keyword) {
    	
    	return doPythonExec(keyword, Collections.emptyList());
    	
    }
    


    @Override
    public Object execute(final Object[] params) throws Exception {
        if(MainContextHolder.get() == null) {
            throw new IllegalStateException("Not running on robot framework runtime.");
        }

        List<Object> variables = new ArrayList<Object>();

        if (params.length > 1) {
        	variables = Arrays.asList(params).subList(1, params.length);
            //variables.addAll(Arrays.asList(params).subList(1, params.length));
        }

        String name = String.valueOf(params[0]);

        return runKeyword(name, variables);
    }
}
