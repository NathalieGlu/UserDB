package ru.nathalie.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.factory.ServerFactory;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class);

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(8080);
        while (true) {
            WebServer webServer = ServerFactory.build();
            webServer.setSocket(ss.accept());
            log.info("Client accepted");
            new Thread(webServer).start();
        }
    }
}
