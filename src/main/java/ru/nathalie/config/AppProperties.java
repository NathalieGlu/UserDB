package ru.nathalie.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class AppProperties {

    private String driver;
    private String url;
    private String dbUsername;
    private String dbPassword;
    private final static Logger log = LoggerFactory.getLogger(AppProperties.class.getName());

    public AppProperties() {

        try (InputStream input = new FileInputStream("/home/nathalie/IdeaProjects/UserDB/src/main/resources/application.yaml");) {
            Properties props = new Properties();
            props.load(input);

            this.driver = props.getProperty("driver");
            this.url = props.getProperty("url");
            this.dbUsername = props.getProperty("dbUsername");
            this.dbPassword = props.getProperty("dbPassword");

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
