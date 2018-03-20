package ru.nathalie.db;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.config.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolC3P0 implements ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(ConnectionPoolC3P0.class.getName());
    private ComboPooledDataSource cpds;

    public ConnectionPoolC3P0(AppProperties appProperties) {
        try {
            cpds = new ComboPooledDataSource();
            cpds.setDriverClass(appProperties.getDriver());
            cpds.setJdbcUrl(appProperties.getUrl());
            cpds.setUser(appProperties.getDbUsername());
            cpds.setPassword(appProperties.getDbPassword());

            cpds.setMaxPoolSize(appProperties.getMaxActive());
            cpds.setMaxStatements(appProperties.getMaxStatements());
        } catch (Exception e) {
            log.error("Exception during pool setting: ", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return cpds.getConnection();
    }
}
