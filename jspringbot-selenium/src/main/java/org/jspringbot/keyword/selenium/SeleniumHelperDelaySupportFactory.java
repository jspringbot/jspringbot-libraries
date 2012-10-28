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

package org.jspringbot.keyword.selenium;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * Add delay support.
 */
public class SeleniumHelperDelaySupportFactory {

    public static SeleniumHelper create(SeleniumHelper helper, long millis) {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(SeleniumHelper.class);
        enhancer.setCallback(new EnhancerCallback(helper, millis));

        return (SeleniumHelper) enhancer.create();
    }

    private static class EnhancerCallback implements MethodInterceptor {

        private SeleniumHelper delegate;

        private long delay;

        private EnhancerCallback(SeleniumHelper delegate, long delay) {
            this.delegate = delegate;
            this.delay = delay;
        }

        @Override
        public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
            Object returnedValue = method.invoke(delegate, objects);

            if(delay > 0) {
                System.out.println(String.format("Delayed after '%s' invocation for %d ms.", method.getName(), delay));
                Thread.sleep(delay);
            }

            return returnedValue;
        }
    }


}
