package ru.nathalie;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;
import ru.nathalie.config.AppProperties;

import javax.sql.DataSource;

public class ConnectionPool {

    private static GenericObjectPool genericObjectPool = null;
    private final AppProperties properties;

    public ConnectionPool() {
        this.properties = new AppProperties();
    }

    public DataSource setPool() throws Exception {

        Class.forName(properties.getDriver());

        genericObjectPool = new GenericObjectPool();
        genericObjectPool.setMaxActive(20);

        ConnectionFactory factory = new DriverManagerConnectionFactory(properties.getUrl(),
                properties.getDbUsername(), properties.getDbPassword());

        PoolableConnectionFactory poolableFactory = new PoolableConnectionFactory(factory,
                genericObjectPool, null, null, false, true);

        return new PoolingDataSource(genericObjectPool);
    }

    public static GenericObjectPool getGenericObjectPool() {
        return genericObjectPool;
    }
}
