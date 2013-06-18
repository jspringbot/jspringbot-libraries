package org.jspringbot.keyword.db;

import org.apache.commons.dbcp.BasicDataSource;

public interface BasicDataSourceManager {

    public static final String DEFAULT_CONNECTION_NAME = "default";

    public void switchConnection(String connectionName);

    public BasicDataSource getCurrentDataSource();

    public BasicDataSource getDataSource(String datasourceName);
}