package org.jspringbot.keyword.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class BasicDataSourceManagerImpl implements BasicDataSourceManager {

    private Resource propertiesResource;

    private int maxActive = 5;

    private int maxWait = 1000;

    private boolean poolPreparedStatements = true;

    private boolean defaultAutoCommit = true;

    private Map<String,BasicDataSource> basicDataSourceMap = new HashMap<String, BasicDataSource>();

    private String currentConnectionName = BasicDataSourceManager.DEFAULT_CONNECTION_NAME;

    public void switchConnection(String connectionName) {
        if (basicDataSourceMap.get(connectionName) == null ) {
            throw new IllegalArgumentException(String.format("Non existing connection name '%s'", connectionName));
        }

        this.currentConnectionName = connectionName;
    }

    @Required
    public void setPropertiesResource(Resource propertiesResource) {
        this.propertiesResource = propertiesResource;
    }

    public void setMaxActive(int maxActive) {
        this.maxActive = maxActive;
    }

    public void setMaxWait(int maxWait) {
        this.maxWait = maxWait;
    }

    public void setPoolPreparedStatements(boolean poolPreparedStatements) {
        this.poolPreparedStatements = poolPreparedStatements;
    }

    public void setDefaultAutoCommit(boolean defaultAutoCommit) {
        this.defaultAutoCommit = defaultAutoCommit;
    }

    public void load() throws IOException {

        Properties properties = new Properties();
        properties.load(propertiesResource.getInputStream());

        String driver = properties.getProperty("db.jdbc.driverClassName");
        String url = properties.getProperty("db.jdbc.url");
        String username = properties.getProperty("db.jdbc.username");
        String passsword = properties.getProperty("db.jdbc.password");

        basicDataSourceMap.put(BasicDataSourceManager.DEFAULT_CONNECTION_NAME, createBasicDataSource(driver, url, username, passsword));

        String dbConnections = properties.getProperty("db.connection.set");

        if (StringUtils.isBlank(dbConnections)) {
            return;
        }

        String dbConnArr[] = StringUtils.split(dbConnections,',') ;

        for(String dbConn:dbConnArr) {
            driver = properties.getProperty(String.format("%s.db.jdbc.driverClassName", dbConn));
            url = properties.getProperty(String.format("%s.db.jdbc.url", dbConn));
            username = properties.getProperty(String.format("%s.db.jdbc.username", dbConn));
            passsword = properties.getProperty(String.format("%s.db.jdbc.password", dbConn));

            basicDataSourceMap.put(dbConn, createBasicDataSource(driver, url, username, passsword));
        }
    }

    private BasicDataSource createBasicDataSource(String driver, String url, String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(username);
        dataSource.setPassword(password);
        dataSource.setMaxActive(maxActive);
        dataSource.setMaxWait(maxWait);
        dataSource.setPoolPreparedStatements(poolPreparedStatements);
        dataSource.setDefaultAutoCommit(defaultAutoCommit);

        return dataSource;
    }

    @Override
    public BasicDataSource getCurrentDataSource() {
        return getDataSource(currentConnectionName);
    }

    @Override
    public BasicDataSource getDataSource(String datasourceName) {
        if (basicDataSourceMap.get(datasourceName) == null ) {
            throw new IllegalArgumentException("Non existing datasourceName");
        }

        return basicDataSourceMap.get(datasourceName);
    }
}
