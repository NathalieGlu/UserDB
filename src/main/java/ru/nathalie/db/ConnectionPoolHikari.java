package ru.nathalie.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.config.AppProperties;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionPoolHikari implements ConnectionPool {
    private static final Logger log = LoggerFactory.getLogger(ConnectionPoolHikari.class.getName());
    private static final String CACHE_PREP = "cachePrepStmts";
    private static final String CACHE_SIZE = "prepStmtCacheSize";
    private static final String CACHE_LIMIT = "prepStmtCacheSqlLimit";
    private static final HikariConfig config = new HikariConfig();
    private static HikariDataSource ds;

    public ConnectionPoolHikari(AppProperties appProperties) {
        try {
            config.setJdbcUrl(appProperties.getUrl());
            config.setUsername(appProperties.getDbUsername());
            config.setPassword(appProperties.getDbPassword());
            config.addDataSourceProperty(CACHE_PREP, "true");
            config.addDataSourceProperty(CACHE_SIZE, appProperties.getCacheSize());
            config.addDataSourceProperty(CACHE_LIMIT, appProperties.getCacheLimit());
            ds = new HikariDataSource(config);
        } catch (Exception e) {
            log.error("Exception during pool setting: ", e);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return ds.getConnection();
    }
}
