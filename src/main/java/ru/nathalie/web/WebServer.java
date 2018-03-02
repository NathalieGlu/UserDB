package ru.nathalie.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.dao.UserDao;

import java.io.*;
import java.net.Socket;

public class WebServer implements Runnable {
    private final static String HTTP_HEADER = "HTTP/1.1 200 OK\r\n" +
            "Server: YarServer/2009-09-09\r\n" +
            "Content-Type: text/html\r\n" +
            "Content-Length: ";
    private final Logger log = LoggerFactory.getLogger(WebServer.class.getName());
    private final UserDao userDao;
    private Socket socket;
    private InputStream is;
    private OutputStream os;

    public WebServer(UserDao userDao) {
        this.userDao = userDao;
    }

    public void setSocket(Socket socket) throws IOException {
        this.socket = socket;
        this.is = socket.getInputStream();
        this.os = socket.getOutputStream();
    }

    @Override
    public void run() {
        log.info("Client processing started");

        readInputHeaders();
        writeResponse();

        try {
            socket.close();
        } catch (IOException e) {
            log.error("Exception: ", e);
        }
        log.info("Client processing finished");
    }

    private void writeResponse() {
        String jsonAnswer = getUsers();
        String response = HTTP_HEADER + jsonAnswer.length() + "\r\nConnection: close\r\n\r\n";
        String result = response + jsonAnswer;
        try {
            os.write(result.getBytes());
            os.flush();
        } catch (IOException e) {
            log.error("Error during response: ", e);
        }
    }

    private void readInputHeaders() {
        BufferedReader br = new BufferedReader(new InputStreamReader(is));
        try {
            while (true) {
                String s = br.readLine();
                if (s == null || s.trim().length() == 0) {
                    break;
                }
            }
        } catch (IOException e) {
            log.error("Error during request: ", e);
        }
    }

    private String getUsers() {
        return userDao.getUsers().toString();
    }
}
