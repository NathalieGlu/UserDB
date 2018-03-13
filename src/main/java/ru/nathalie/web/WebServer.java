package ru.nathalie.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.router.Router;

import java.io.IOException;
import java.net.Socket;

public class WebServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class.getName());
    private final Router router;
    private Socket socket;

    public WebServer(Router router) {
        this.router = router;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        log.info("Client processing started");
        try {
            router.setRouter();
        } catch (Exception e) {
            log.error("Error setting router", e);
        }

        try {
            InputStreamData isData = new InputStreamData(socket.getInputStream());
            router.parseHeaders(isData, socket);
            socket.close();
        } catch (IOException e) {
            log.error("Exception: ", e);
        }
        log.info("Client processing finished");
    }
}
