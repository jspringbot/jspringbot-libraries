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

package org.jspringbot.keyword.csv;

import org.apache.commons.lang.Validate;
import org.jspringbot.syntax.HighlightRobotLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * CSV main helper class
 */
public class CSVHelper {

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(CSVHelper.class);


    private CSVState currentState;

    private Map<String, CSVState> states = new HashMap<String, CSVState>();

    public void parseCSVString(String name, String csv) throws IOException {
        currentState = new CSVState(name);
        currentState.parseCSVString(csv);

        states.put(name, currentState);
    }

    public void parseCSVResource(String name, String resource) throws IOException {
        currentState = new CSVState(name);
        currentState.parseCSVResource(resource);

        states.put(name, currentState);
    }

    public void parseCSVString(String csv) throws IOException {
        parseCSVString(String.valueOf(System.currentTimeMillis()), csv);
    }

    public void parseCSVResource(String resource) throws IOException {
        parseCSVResource(String.valueOf(System.currentTimeMillis()), resource);
    }

    public void createAlias(String name) {
        LOG.html("State <b>%s</b> with alias <b>%s</b>", currentState.getName(), name);

        states.put(name, currentState);
        currentState = states.get(name);
    }

    public void switchState(String name) {
        Validate.isTrue(states.containsKey(name), String.format("CSV state with name '%s' not found.", name));

        LOG.html("Switching to <b>%s</b>", name);

        currentState = states.get(name);
    }

    public void startDisjunction() {
        currentState.startDisjunction();
    }

    public void endDisjunction() {
        currentState.endDisjunction();
    }

    public void startConjunction() {
        currentState.startConjunction();
    }

    public void endConjunction() {
        currentState.endConjunction();
    }

    public List<String[]> getLines() {
        return currentState.getLines();
    }

    public void setHeaders(String headers) throws IOException {
        currentState.setHeaders(headers);
    }

    public void setFirstLineAsHeader() {
        currentState.setFirstLineAsHeader();
    }

    public void createCriteria() {
        currentState.createCriteria();
    }

    public void addColumnNameEqualsRestriction(String name, String value) {
        currentState.addColumnNameEqualsRestriction(name, value);
    }

    public void addColumnIndexEqualsRestriction(int index, String value) {
        currentState.addColumnIndexEqualsRestriction(index, value);
    }

    public int projectCount() {
        return currentState.projectCount();
    }

    public void createStateFromList(String name) {
        currentState = currentState.listAsNewState(name);
        states.put(name, currentState);
    }

    public List<String[]> list() {
        return currentState.list();
    }

    public String[] firstResult() {
        return currentState.firstResult();
    }

    public String firstResultColumnIndex(int index) {
        return currentState.firstResultColumnIndex(index);
    }

    public String firstResultColumnName(String name) {
        return currentState.firstResultColumnName(name);
    }

    public String lastResultColumnIndex(int index) {
        return currentState.lastResultColumnIndex(index);
    }

    public String lastResultColumnName(String name) {
        return currentState.lastResultColumnName(name);
    }

    public List<String> listColumnName(String name) {
        return currentState.listColumnName(name);
    }

    public List<String> listColumnIndex(int index) {
        return currentState.listColumnIndex(index);
    }

    public String[] lastResult() {
        return currentState.lastResult();
    }

    public String getColumnValue(String[] line, String name) {
        return currentState.getColumnValue(line, name);
    }
}
