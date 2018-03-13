package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ExcHandler;
import ru.nathalie.model.dao.BalanceDao;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDao;
import ru.nathalie.model.dao.UserDaoImpl;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class.getName());
    static final String HTTP_NOT_FOUND = "404 Not Found\n";
    static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    static final String HTTP_OK = "200 OK\n";
    private final ExcHandler handler;
    private OutputStream os;
    final UserDao userDao;
    final BalanceDao balanceDao;

    Controller(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ExcHandler handler) {
        this.userDao = userDao;
        this.balanceDao = balanceDao;
        this.handler = handler;
    }

    public void setSocket(Socket socket) throws IOException {
        this.os = socket.getOutputStream();
        this.handler.setStream(socket.getOutputStream());
    }

    void throwException(Throwable throwable) {
        handler.printException(throwable);
    }

    void writeResponse(String response) {
        try {
            log.info("Printing response...");
            os.write(response.getBytes());
            os.flush();
        } catch (IOException e) {
            log.error("Error during response: ", e);
        }
    }
}
