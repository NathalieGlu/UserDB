package ru.nathalie.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private static final String DRIVER_PROPERTY_NAME = "driver";
    private static final String URL_PROPERTY_NAME = "url";
    private static final String DB_USERNAME_PROPERTY_NAME = "dbUsername";
    private static final String DB_PASSWORD_PROPERTY_NAME = "dbPassword";
    private final static Logger log = LoggerFactory.getLogger(AppProperties.class.getName());
    private String driver;
    private String url;
    private String dbUsername;
    private String dbPassword;

    public AppProperties() {

        try (InputStream input = new FileInputStream(System.getProperty("user.dir") + "src/main/resources/application.yaml")) {
            Properties props = new Properties();
            props.load(input);

            this.driver = props.getProperty(DRIVER_PROPERTY_NAME);
            this.url = props.getProperty(URL_PROPERTY_NAME);
            this.dbUsername = props.getProperty(DB_USERNAME_PROPERTY_NAME);
            this.dbPassword = props.getProperty(DB_PASSWORD_PROPERTY_NAME);

            log.info("Loaded config");
        } catch (Exception e) {
            log.error("Error: ", e);
        }
    }

    public String getDriver() {
        return driver;
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
}
