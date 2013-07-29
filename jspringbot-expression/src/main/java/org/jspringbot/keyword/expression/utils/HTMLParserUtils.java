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

package org.jspringbot.keyword.expression.utils;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HTMLParserUtils {
	
	public static List<String> getCSSFileLinks(String html) {
		Document doc = Jsoup.parse(html);
		Elements linkHref = doc.select("link[href]");
		System.out.println(html);
		List<String> cssFileLinks = new ArrayList<String>();
		for (Element link : linkHref) {
        	if (link.attr("abs:href").endsWith(".css")) {
        		cssFileLinks.add(link.attr("abs:href"));
        	}
        }
		return cssFileLinks;
	}
	
	public static List<String> getJSFileLinks(String html) {
		Document doc = Jsoup.parse(html);
		Elements scriptSrc = doc.select("script[src]");
		List<String> jsFileLinks = new ArrayList<String>();
		for (Element script : scriptSrc) {
			if (script.attr("abs:src").endsWith(".js")) {
				jsFileLinks.add(script.attr("abs:src"));
			}
		}
		return jsFileLinks;
	}
	
}
