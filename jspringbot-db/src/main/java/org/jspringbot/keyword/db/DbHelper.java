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

package org.jspringbot.keyword.db;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.*;
import org.hibernate.jdbc.Work;
import org.hibernate.type.Type;
import org.jspringbot.syntax.HighlightRobotLogger;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;

import java.io.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbHelper {

    private static final Logger LOGGER = Logger.getLogger(DbHelper.class);

    public static final HighlightRobotLogger LOG = HighlightRobotLogger.getLogger(DbHelper.class);

    private static final Pattern NAMED_PARAMETER_PATTERN = Pattern.compile(":[a-z0-9$_]+", Pattern.CASE_INSENSITIVE);

    protected SessionFactory factory;

    protected Session session;

    protected Transaction transaction;

    protected SQLQuery query;

    protected List records;

    protected Properties externalQueries = new Properties();

    protected String schema;

    protected String useSchemaSyntax = "use %s";

    protected Map<String, String> literalSubstitution = new HashMap<String, String>();

    public DbHelper(SessionFactory factory) {
        this.factory = factory;
    }

    public void setUseSchemaSyntax(String useSchemaSyntax) {
        this.useSchemaSyntax = useSchemaSyntax;
    }

    private void addExternalQueries(File file) throws IOException {
        String filename = file.getName();

        Properties properties = new Properties();

        if(StringUtils.endsWith(filename, ".properties")) {
            properties.load(new FileReader(file));
        } else if(StringUtils.endsWith(filename, ".xml")) {
            properties.loadFromXML(new FileInputStream(file));
        }

        externalQueries.putAll(properties);
    }

    public void init() {
        ResourceEditor editor = new ResourceEditor();
        editor.setAsText("classpath:db-queries/");
        Resource dbDirResource = (Resource) editor.getValue();

        boolean hasDBDirectory = true;
        boolean hasDBProperties = true;

        if(dbDirResource != null) {
            try {
                File configDir = dbDirResource.getFile();

                if(configDir.isDirectory()) {
                    File[] propertiesFiles = configDir.listFiles(new FileFilter() {
                        @Override
                        public boolean accept(File file) {
                            return StringUtils.endsWith(file.getName(), ".properties") || StringUtils.endsWith(file.getName(), ".xml");
                        }
                    });

                    for(File propFile : propertiesFiles) {
                        addExternalQueries(propFile);
                    }
                }
            } catch(IOException ignore) {
                hasDBDirectory = false;
            }
        }

        editor.setAsText("classpath:db-queries.properties");
        Resource dbPropertiesResource = (Resource) editor.getValue();

        if(dbPropertiesResource != null) {
            try {
                if(dbPropertiesResource.getFile().isFile()) {
                    addExternalQueries(dbPropertiesResource.getFile());
                }
            } catch(IOException e) {
                hasDBProperties = false;
            }
        }

        editor.setAsText("classpath:db-queries.xml");
        Resource dbXMLResource = (Resource) editor.getValue();

        if(dbXMLResource != null && !hasDBProperties) {
            try {
                if(dbXMLResource.getFile().isFile()) {
                    addExternalQueries(dbXMLResource.getFile());
                }
            } catch(IOException e) {
                hasDBProperties = false;
            }
        }

        if(!hasDBDirectory && !hasDBProperties) {
            LOGGER.warn("No query sources found.");
        }

        begin();
    }

    public void begin() {
        session = factory.openSession();
        transaction = session.beginTransaction();
    }

    public void commit() {
        transaction.commit();
        session.close();
    }

    public void rollback() {
        transaction.rollback();
        session.close();
    }

    public void useSchema(final String param) {
        schema = param;

        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();

                try {
                    LOG.createAppender()
                            .appendBold("Use Schema:")
                            .appendProperty("Schema", param)
                            .log();

                    stmt.execute(String.format(useSchemaSyntax, schema));
                } finally {
                    stmt.close();
                }
            }
        });
    }

    public void createQuery(String queryString) {
        validateSchema();
        literalSubstitution.clear();

        LOG.createAppender()
                .appendBold("Create Query")
                .appendSQL(SQLFormatter.prettyPrint(queryString))
                .log();

        query = session.createSQLQuery(queryString);

        records = null;
    }

    public void createQueryByName(String queryName) {
        if (!externalQueries.containsKey(queryName)) {
            throw new IllegalArgumentException("query name not found in list.");
        }

        String sql = externalQueries.getProperty(queryName);

        LOG.createAppender()
                .appendBold("Create Query By Name:")
                .appendProperty("Name", queryName)
                .log();

        createQuery(sql);
    }

    public void setStringParameter(String key, String value) {
        validateQuery();

        LOG.createAppender()
                .appendBold("Set String Parameter:")
                .appendProperty("property", key)
                .appendProperty("value", value)
                .log();

        literalSubstitution.put(key, String.format("'%s'", LiteralEscapeUtils.escapeString(value)));

        query.setString(key, value);
    }

    public void setIntegerParameter(String key, Integer value) {
        validateQuery();

        LOG.createAppender()
                .appendBold("Set Integer Parameter:")
                .appendProperty("property", key)
                .appendProperty("value", value)
                .log();

        literalSubstitution.put(key, String.valueOf(value));

        query.setInteger(key, value);
    }

    public void setLongParameter(String key, Long value) {
        validateQuery();

        LOG.createAppender()
                .appendBold("Set Long Parameter:")
                .appendProperty("property", key)
                .appendProperty("value", value)
                .log();

        literalSubstitution.put(key, String.valueOf(value));

        query.setLong(key, value);
    }

    private void addLiteralSubstitution(String key, Object[] obj) {
        StringBuilder buf = new StringBuilder();

        for(Object item : obj) {
            if(buf.length() > 0) {
                buf.append(", ");
            }

            if(String.class.isInstance(item)) {
                buf.append(String.format("'%s'", LiteralEscapeUtils.escapeString((String) item)));
            } else {
                buf.append(String.valueOf(item));
            }
        }

        literalSubstitution.put(key, buf.toString());
    }

    public void setParameterList(String key, Object parameterList) {
        validateQuery();

        if (parameterList instanceof Object[]) {
            query.setParameterList(key, (Object[]) parameterList);

            LOG.createAppender()
                    .appendBold("Set Parameter List:")
                    .appendProperty("property", key)
                    .appendProperty("values", Arrays.asList((Object[]) parameterList))
                    .log();

            addLiteralSubstitution(key, (Object[]) parameterList);

        } else if (parameterList instanceof Collection) {
            query.setParameterList(key, (Collection) parameterList);

            LOG.createAppender()
                    .appendBold("Set Parameter List:")
                    .appendProperty("property", key)
                    .appendProperty("values", parameterList)
                    .log();

            addLiteralSubstitution(key, ((Collection) parameterList).toArray());
        } else {
            throw new IllegalArgumentException("ParameterList Type is not supported.");
        }
    }

    public void addResultColumn(String name, String type) {
        validateQuery();

        LOG.createAppender()
                .appendBold("Add Result Column:")
                .appendProperty("name", name)
                .appendProperty("type", type)
                .log();

        Field field = null;
        try {
            field = Hibernate.class.getField(type);
            query.addScalar(name, (Type) field.get(Hibernate.class));

        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Invalid type '%s'", type));
        }
    }

    public int executeUpdate() {
        validateQuery();

        String queryString = SQLFormatter.prettyPrint(sqlSubstitute(query.getQueryString()));
        int affectedRows = query.executeUpdate();

        LOG.createAppender()
                .appendBold("Execute Update ('%d' affected rows) :", affectedRows)
                .appendSQL(queryString)
                .log();

        return affectedRows;
    }

    public void executeQuery() {
        String queryString = SQLFormatter.prettyPrint(sqlSubstitute(query.getQueryString()));

        records = query.list();
        LOG.createAppender()
                .appendBold("Execute Update ('%d' records retrieved) :", records.size())
                .appendSQL(queryString)
                .log();
    }

    private String sqlSubstitute(String queryString) {
        StringBuilder buf = new StringBuilder(queryString);

        Matcher matcher = NAMED_PARAMETER_PATTERN.matcher(buf);

        int index = 0;
        while(matcher.find(index)) {
            String param = matcher.group().substring(1);
            if(literalSubstitution.containsKey(param)) {
                String replacement = literalSubstitution.get(param);
                buf.replace(matcher.start(), matcher.end(), replacement);

                index = matcher.start() + replacement.length();
            } else {
                index = matcher.end();
            }
        }

        return buf.toString();
    }

    public Object getUniqueResult() {
        validateRecord();

        return records.get(0);
    }

    public void recordShouldNotBeEmpty() {
        validateRecord();

        if (records.isEmpty()) {
            throw new IllegalStateException("Records is empty");
        }
    }

    public void projectedCountShouldBe(int count) {
        int projectedCount = getProjectedCount();

        if (projectedCount != count) {
            throw new IllegalStateException(String.format("Expected projected count '%d' but was %d.", count, projectedCount));
        }
    }

    public void projectedCountIsNotZero() {
        int projectedCount = getProjectedCount();
        if (projectedCount <= 0) {
            throw new IllegalStateException(String.format("Expected projected count should not be zero but was %d", projectedCount));
        }
    }

    public int getProjectedCount() {
        validateRecord();

        int projectedCount = ((Number) records.get(0)).intValue();

        LOG.createAppender()
                .appendBold("Get Projected Count")
                .appendProperty("Result", projectedCount)
                .log();

        return projectedCount;
    }

    public void projectedCountIsZero() {
        int projectedCount = getProjectedCount();
        if (projectedCount != 0) {
            throw new IllegalStateException(String.format("Expected projected count should be zero but was %d", projectedCount));
        }
    }

    public void projectedCountShouldBeEqual(int expectedCount) {
        int projectedCount = getProjectedCount();

        if (projectedCount != expectedCount) {
            throw new IllegalStateException(String.format("Actual count %d should be equal to expected count %d", projectedCount, expectedCount));
        }
    }

    public List getValuesAtColumn(int columnNum) {
        validateRecord();

        List<Object> list = new ArrayList<Object>();

        for (Object resultElement : records) {
            if (resultElement instanceof Object[]) {
                Object[] obj = (Object[]) resultElement;

                if (columnNum >= obj.length) {
                    throw new IllegalArgumentException("Column number does not exists");
                }

                list.add(obj[columnNum]);
            } else {
                if (columnNum != 0) {
                    throw new IllegalArgumentException("Column number does not exists");
                }

                list.add(resultElement);

            }
        }

        return list;
    }

    public void recordAtColumnShouldContain(int columnNum, Object expectedValue) {
        List list = getValuesAtColumn(columnNum);
        if (!list.contains(expectedValue)) {
            throw new IllegalArgumentException(String.format("Expected value '%s' is not in column '%d' list", String.valueOf(expectedValue), columnNum));
        }
    }

    public Object getRecordAtColumn(int columnNum) {
        return getRecordAtRowColumn(0, columnNum);
    }

    public Object getRecordAtRowColumn(int rowNum, int columnNum) {
        validateRecord();

        Object objectRow = records.get(rowNum);
        if (objectRow instanceof Object[]) {
            Object[] obj = (Object[]) objectRow;

            if (columnNum >= obj.length) {
                throw new IllegalArgumentException("Column number does not exists");
            }

            return obj[columnNum];
        } else {
            if (columnNum != 0) {
                throw new IllegalArgumentException("Column number does not exists");
            }

            return objectRow;
        }
    }

    public void recordAtRowColumnShouldContain(int rowNum, int columnNum, Object expectedValue) {
        validateRecord();

        Object objectRow = records.get(rowNum);
        if (objectRow instanceof Object[]) {
            Object[] obj = (Object[]) objectRow;

            if (columnNum >= obj.length) {
                throw new IllegalArgumentException("Column number does not exists");
            }

            if (!obj[columnNum].equals(expectedValue)) {
                throw new IllegalArgumentException(String.format("Expected value '%s' is not in at '%d','%d'", String.valueOf(expectedValue), rowNum, columnNum));
            }
        } else {
            if (columnNum != 0) {
                throw new IllegalArgumentException("Column number does not exists");
            }

            if (!objectRow.equals(expectedValue)) {
                throw new IllegalArgumentException(String.format("Expected value '%s' is not in at '%d','%d'", String.valueOf(expectedValue), rowNum, columnNum));
            }
        }
    }

    private void validateSchema() {
        // no need to check schema
        // default might be selected
    }

    private void validateQuery() {
        validateSchema();
        if (query == null) {
            throw new IllegalStateException("Create Query keyword was not executed") ;
        }
    }

    private void validateRecord() {
        validateQuery();
        if (records == null) {
            throw new IllegalStateException("Execute Query keyword was not executed") ;
        }
    }

}
