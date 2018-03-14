package ru.nathalie.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {
    private static final String RESOURCES_YAML = "application.yaml";
    private static final String DRIVER_PROPERTY_NAME = "driver";
    private static final String PORT_PROPERTY_NAME = "port";
    private static final String URL_PROPERTY_NAME = "url";
    private static final String DB_USERNAME_PROPERTY_NAME = "dbUsername";
    private static final String DB_PASSWORD_PROPERTY_NAME = "dbPassword";
    private static final String MAX_ACTIVE_PROPERTY_NAME = "max_active";
    private final static Logger log = LoggerFactory.getLogger(AppProperties.class.getName());
    private String driver;
    private String url;
    private String dbUsername;
    private String dbPassword;
    private Integer port;
    private Integer maxActive;

    public AppProperties() {

        try (InputStream input = new FileInputStream(getClass().getClassLoader().getResource(RESOURCES_YAML).getFile())) {
            Properties props = new Properties();
            props.load(input);

            this.driver = props.getProperty(DRIVER_PROPERTY_NAME);
            this.port = Integer.valueOf(props.getProperty(PORT_PROPERTY_NAME));
            this.url = props.getProperty(URL_PROPERTY_NAME);
            this.dbUsername = props.getProperty(DB_USERNAME_PROPERTY_NAME);
            this.dbPassword = props.getProperty(DB_PASSWORD_PROPERTY_NAME);
            this.maxActive = Integer.valueOf(props.getProperty(MAX_ACTIVE_PROPERTY_NAME));

            log.info("Loaded config");
        } catch (Exception e) {
            log.error("Error during application properties initialization: ", e);
        }
    }

    public String getDriver() {
        return driver;
    }

    public Integer getPort() {
        return port;
    }

    public String getUrl() {
        return url;
    }

    public String getDbUsername() {
        return dbUsername;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public Integer getMaxActive() {
        return maxActive;
    }
}
