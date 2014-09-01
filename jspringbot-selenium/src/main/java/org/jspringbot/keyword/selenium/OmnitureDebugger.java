package org.jspringbot.keyword.selenium;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Map.Entry;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.beans.factory.annotation.Autowired;

public class OmnitureDebugger extends SeleniumHelper {
	public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(OmnitureDebugger.class);
	
	@Autowired
	protected SeleniumHelper helper;

	// Values are set using OmnitureDebuggerBean
	private String omnitureDebuggerLocation;
	private int omnitureDebuggerWaitTimeInMillis;

	private static final String OMNITURE_DEBUGGER_WINDOW_NAME = "stats_debugger";
	private static final String OMNITURE_DEBUGGER_URL_DECODE_CHECKBOX = "url_decode";

	public OmnitureDebugger() {
	}
    
	public ArrayList<String> getListOfSiteCatalystVariables() {
		String htmlSource = helper.getHTMLSourceOfOmnitureDebuggerWindow(
				getStatsDebuggerLocation(), OMNITURE_DEBUGGER_WINDOW_NAME,
				OMNITURE_DEBUGGER_URL_DECODE_CHECKBOX,
				getOmnitureDebuggerWaitTimeInMillis());
		return getSiteCatalystValuesListFromHtml(htmlSource);
	}

	public HashMap<String, String> getMapOfSiteCatalystVariables() {
		String htmlSource = helper.getHTMLSourceOfOmnitureDebuggerWindow(
				getStatsDebuggerLocation(), OMNITURE_DEBUGGER_WINDOW_NAME,
				OMNITURE_DEBUGGER_URL_DECODE_CHECKBOX,
				getOmnitureDebuggerWaitTimeInMillis());
		return getSiteCatalystMapFromHtmlSource(htmlSource);
	}
	
	public void siteCatalystVariableMapShouldContain(HashMap<String,String> map, String scVar, String expectedValue){
		try {
			String actualValue = map.get(scVar);
			LOG.createAppender()
					.appendBold("Site Catalyst Variable And Value:")
					.appendProperty("scVar", scVar)
					.appendProperty("Expected Value:", expectedValue)
					.appendProperty("Actual Value:", actualValue)
					.log();
			if (!actualValue.equals(expectedValue)) {
				throw new AssertionError("The value of the variable contained in the map is not as expected.");
			}
		} catch (Exception e) {
			LOG.createAppender()
					.appendBold("No Site Catalyst Variable(s) Found In Map.")
					.log();
		}
    }
    
    public void siteCatalystVariableListShouldContain(ArrayList<String> aList, String expectedKeyValuePair){
		try {
			LOG.createAppender()
					.appendBold("Site Catalyst Variable And Value:")
					.appendProperty("Expected Key-Value Pair In List:", expectedKeyValuePair)
					.appendCode(printList(aList))
					.log();
			if (!aList.contains(expectedKeyValuePair)) {
				throw new AssertionError("The expected key-value pair is not present in the list.");
			}
		} catch (Exception e) {
			LOG.createAppender()
					.appendBold("No Site Catalyst Variable(s) Found In List.")
					.log();
		}
    }

	private String getStatsDebuggerLocation() {
		return "javascript:(window.open(\"\",\""
				+ OMNITURE_DEBUGGER_WINDOW_NAME
				+ "\",\"width=600,height=600,location=0,menubar=0,status=1,toolbar=0,resizable=1,scrollbars=1\").document.write(\"<script language='JavaScript' src='"
				+ getOmnitureDebuggerLocation() 
				+ "'></script>\"));";
	}
       
    private ArrayList<String> getSiteCatalystValuesListFromHtml(String htmlSource) {   
    	ArrayList<String> scVariables = null;
		try {
			scVariables = (ArrayList<String>) parseAndReturnSiteCatalystVariables(htmlSource);
			LOG.createAppender()
					.appendBold("Site Catalyst Variables And Values:")
					.appendCode(printList(scVariables)).log();
			return (ArrayList<String>) scVariables;
		} catch (Exception e) {
			LOG.createAppender()
					.appendBold("No Site Catalyst Variable(s) Found.").log();
	        return null;
		}
    }
    
    private HashMap<String,String> getSiteCatalystMapFromHtmlSource(String htmlSource) {
		Map<String, String> scVariables = new HashMap<String, String>();
		List<String> variables = parseAndReturnSiteCatalystVariables(htmlSource);
		try {
			for (String temp : variables) {
				String[] holder = temp.split("=");
				scVariables.put(holder[0], holder[1]);
			}
			LOG.createAppender()
					.appendBold("Site Catalyst Variables And Values:")
					.appendCode(printMap(sortByKeys(scVariables))).log();
			return (HashMap<String, String>) scVariables;
		} catch (Exception e) {
			LOG.createAppender()
					.appendBold("No Site Catalyst Variable(s) Found.").log();
			return null;
		}
    }
    
	private String printMap(Map<String, String> map) {
		StringBuilder sb = new StringBuilder();
		Iterator<Entry<String, String>> iter = map.entrySet().iterator();
		while (iter.hasNext()) {
			Entry<String, String> entry = iter.next();
			sb.append(entry.getKey());
			sb.append(" = ");
			sb.append(entry.getValue());
			if (iter.hasNext()) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}

	private String printList(ArrayList<String> list) {
		StringBuilder sb = new StringBuilder();
		ListIterator<String> listIterator = list.listIterator();
		while (listIterator.hasNext()) {
			sb.append(listIterator.next());
			if (listIterator.hasNext()) {
				sb.append('\n');
			}
		}
		return sb.toString();
	}
    
	private List<String> parseAndReturnSiteCatalystVariables(String htmlSource) {
		Document doc = Jsoup.parse(htmlSource);
		Elements tds = doc.select("td");
		String[] tdList = null;
		for (Element td : tds) {
			if (td.text().contains("Image")) {
				tdList = td.html().split("<br />");
			}
		}
		try {
			// Out of the 'for loop' to only evaluate/parse the last Image triggered.
			List<String> actualList = new ArrayList<String>(Arrays.asList(tdList));
			actualList.remove(0); // remove from list ---- <span style="font:bold 11px arial,sans-serif;color:#000000;">Image</span>
			return actualList;
		} catch (NullPointerException npe) {
			// LOG.createAppender().appendBold("No Image Found!").log();
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	private HashMap<String, String> sortByKeys(Map<String, String> scVariables) {
		List list = new LinkedList(scVariables.entrySet());
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getKey()).compareTo(((Map.Entry) (o2)).getKey());
			}
		});
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return (HashMap<String, String>) sortedHashMap;
	}

	public void setOmnitureDebuggerLocation(String location) {
		this.omnitureDebuggerLocation = location;
	}

	public void setOmnitureDebuggerWaitTimeInMillis(int waitTime) {
		this.omnitureDebuggerWaitTimeInMillis = waitTime;
	}

	public String getOmnitureDebuggerLocation() {
		return omnitureDebuggerLocation;
	}

	public int getOmnitureDebuggerWaitTimeInMillis() {
		return omnitureDebuggerWaitTimeInMillis;
	}

}
