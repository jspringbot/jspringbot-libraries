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

import org.hibernate.*;
import org.hibernate.jdbc.Work;
import org.hibernate.type.Type;
import org.jspringbot.JSpringBotLogger;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

public class DbHelper {

    public static final JSpringBotLogger LOG = JSpringBotLogger.getLogger(DbHelper.class);

    protected SessionFactory factory;

    protected Session session;

    protected Transaction transaction;

    protected SQLQuery query;

    protected List records;

    protected Properties externalQueries;

    protected String schema;

    public DbHelper(SessionFactory factory) {
        this.factory = factory;
    }

    public void setQuerySource(Resource source) {
        try {
            externalQueries = new Properties();
            externalQueries.load(new InputStreamReader(source.getInputStream()));
        } catch (IOException e) {
            throw new IllegalStateException("Error loading queries.");
        }
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
            public void execute(Connection connection) throws SQLException {
                Statement stmt = connection.createStatement();

                try {
                    LOG.info("use " + schema);
                    stmt.execute("use " + schema);
                } finally {
                    stmt.close();
                }
            }
        });
    }

    public void createQuery(String queryString) {
        validateSchema();
        query = session.createSQLQuery(queryString);

        records = null;
    }

    public void createQueryByName(String queryName) {
        if (!externalQueries.containsKey(queryName)) {
            throw new IllegalArgumentException("query name not found in list.");
        }

        createQuery(externalQueries.getProperty(queryName));
    }

    public void setStringParameter(String key, String value) {
        validateQuery();

        query.setString(key, value);
    }

    public void setIntegerParameter(String key, Integer value) {
        validateQuery();

        query.setInteger(key, value);
    }

    public void setParameterList(String key, Object parameterList) {
        validateQuery();

        if (parameterList instanceof Object[]) {
            query.setParameterList(key, (Object[]) parameterList);
        } else if (parameterList instanceof Collection) {
            query.setParameterList(key, (Collection) parameterList);
        } else {
            throw new IllegalArgumentException("ParameterList Type is not supported.");
        }
    }

    public void addResultColumn(String name, String type) {
        validateQuery();

        Field field = null;
        try {
            field = Hibernate.class.getField(type);
            query.addScalar(name, (Type) field.get(Hibernate.class));

        } catch (Exception e) {
            throw new IllegalArgumentException(String.format("Invalid type '%s'", type));
        }
    }

    public void executeQuery() {
        LOG.info("\nQuery string: " + query.getQueryString());
        records = query.list();
        LOG.info("Record Size: " + records.size());
    }

    public void recordShouldNotBeEmpty() {
        validateRecord();

        if (records.isEmpty()) {
            throw new IllegalStateException("Records is empty");
        }
    }

    public void projectedCountIsNotZero() {
        validateRecord();

        int projectedCount = ((Number) records.get(0)).intValue();
        if (projectedCount <= 0) {
            throw new IllegalStateException(String.format("Expected projected count should not be zero but was %d", projectedCount));
        }
    }

    public void projectedCountShouldBe(int count) {
        validateRecord();

        int projectedCount = ((Number) records.get(0)).intValue();
        if (projectedCount != count) {
            throw new IllegalStateException(String.format("Expected projected count '%d' but was %d.", count, projectedCount));
        }
    }


    public void projectedCountIsZero() {
        validateRecord();

        int projectedCount = ((Number) records.get(0)).intValue();
        if (projectedCount != 0) {
            throw new IllegalStateException(String.format("Expected projected count should be zero but was %d", projectedCount));
        }
    }

    public void recordCountShouldBeEqual(int expectedCount) {
        validateRecord();

        int actualCount = ((Number) records.get(0)).intValue();
        if (actualCount != expectedCount) {
            throw new IllegalStateException(String.format("Actual count %d should be equal to expected count %d", actualCount, expectedCount));
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
        if (schema == null) {
            throw new IllegalStateException("No schema selected") ;
        }
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
