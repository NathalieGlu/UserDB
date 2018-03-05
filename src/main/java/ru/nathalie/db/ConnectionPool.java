package ru.nathalie.db;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.config.AppProperties;

import javax.sql.DataSource;
import java.util.stream.IntStream;

public class ConnectionPool {

    private static final int MAX_ACTIVE = 20;
    private static final Logger log = LoggerFactory.getLogger(ConnectionFactory.class.getName());
    private static GenericObjectPool genericObjectPool;
    private final AppProperties properties;

    public ConnectionPool(AppProperties appProperties) {
        this.properties = appProperties;
    }

    public DataSource setPool() {
        try {
            Class.forName(properties.getDriver());

            genericObjectPool = new GenericObjectPool();
            genericObjectPool.setMinIdle(MAX_ACTIVE);
            genericObjectPool.setMaxIdle(MAX_ACTIVE);
            genericObjectPool.setMaxActive(MAX_ACTIVE);

            ConnectionFactory factory = new DriverManagerConnectionFactory(properties.getUrl(),
                    properties.getDbUsername(), properties.getDbPassword());

            PoolableConnectionFactory poolableFactory = new PoolableConnectionFactory(factory,
                    genericObjectPool, null, null, false, true);
        } catch (ClassNotFoundException e) {
            log.error("Driver not found: ", e);
        }

        IntStream.range(0, MAX_ACTIVE)
                .forEach(i -> {
                    try {
                        genericObjectPool.addObject();
                    } catch (Exception e) {
                        log.error("Exception: ", e);

                    }
                });

        return new PoolingDataSource(genericObjectPool);
    }
}
