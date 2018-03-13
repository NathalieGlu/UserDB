package ru.nathalie.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.config.AppProperties;
import ru.nathalie.factory.ServerFactory;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class.getName());
    private static ServerFactory factory = new ServerFactory();

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {
        WebServer webServer;
        AppProperties properties;
        try {
            webServer = (WebServer) factory.setClass(WebServer.class);
            properties = (AppProperties)factory.setClass(AppProperties.class);
        } catch (Exception e) {
            log.error("Error during class injection: ", e);
            throw new IOException();
        }

        ServerSocket ss = new ServerSocket(properties.getPort());
        while (true) {
            webServer.setSocket(ss.accept());
            log.info("Client accepted");
            new Thread(webServer).start();
        }
    }
}
