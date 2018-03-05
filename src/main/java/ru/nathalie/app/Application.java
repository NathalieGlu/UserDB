package ru.nathalie.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.factory.ServerFactory;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.net.ServerSocket;

public class Application {
    private final static Logger log = LoggerFactory.getLogger(Application.class);
    private final static int PORT = 8080;

    @SuppressWarnings("InfiniteLoopStatement")
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(PORT);
        WebServer webServer = ServerFactory.build();

        while (true) {
            webServer.setSocket(ss.accept());
            log.info("Client accepted");
            new Thread(webServer).start();
        }
    }
}
