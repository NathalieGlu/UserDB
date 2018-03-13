package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ExcHandler;
import ru.nathalie.model.dao.BalanceDao;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDao;
import ru.nathalie.model.dao.UserDaoImpl;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.model.iostream.OutputStreamData;
import ru.nathalie.web.WebServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Controller {
    private static final Logger log = LoggerFactory.getLogger(WebServer.class.getName());
    private static final String HTTP_NOT_FOUND = "404 Not Found\n";
    private static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    private static final String HTTP_OK = "200 OK\n";
    private final UserDao userDao;
    private final BalanceDao balanceDao;
    private final ExcHandler handler;
    private OutputStream os;

    public Controller(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ExcHandler handler) {
        this.userDao = userDao;
        this.balanceDao = balanceDao;
        this.handler = handler;
    }

    public void setSocket(Socket socket) throws IOException {
        this.os = socket.getOutputStream();
        this.handler.setStream(socket.getOutputStream());
    }

    public void getUsers(InputStreamData is, OutputStreamData os) {
        String jsonAnswer = userDao.getUsers();
        os.setCode(HTTP_OK);
        os.setContentLength(String.valueOf(jsonAnswer.length()));
        os.setMessage(jsonAnswer);

        log.info("Got users from database");
        writeResponse(os.getBody());
    }

    public void getBalance(InputStreamData is, OutputStreamData os) {
        try {
            String args = is.getArgs();
            Integer id = getId(args);
            String jsonAnswer = balanceDao.getBalance(id);

            if (jsonAnswer != null) {
                os.setCode(HTTP_OK);
                os.setContentLength(String.valueOf(jsonAnswer.length()));
                os.setMessage(jsonAnswer);
                writeResponse(os.getBody());
            } else {
                log.error("user_id not found!");
                throwException(new IOException(HTTP_NOT_FOUND));
            }
        } catch (NumberFormatException e) {
            log.error("Bad request!");
            throwException(new IOException(HTTP_BAD_REQUEST));
        }
    }

    public void throwException(Throwable throwable) {
        handler.printException(throwable);
    }

    private void writeResponse(String response) {
        try {
            log.info("Printing response...");
            os.write(response.getBytes());
            os.flush();
        } catch (IOException e) {
            log.error("Error during response: ", e);
        }
    }

    private int getId(String mapping) {
        return Integer.parseInt(mapping.substring(mapping.indexOf("=") + 1, mapping.length()));
    }
}
