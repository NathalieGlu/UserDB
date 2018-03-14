package ru.nathalie.controller;

import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.BalanceDao;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDao;
import ru.nathalie.model.dao.UserDaoImpl;
import ru.nathalie.model.data.ResponseData;

import java.io.OutputStream;

class Controller {
    static final String HTTP_NOT_FOUND = "404 Not Found\n";
    static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    private static final String HTTP_OK = "200 OK\n";
    final UserDao userDao;
    final BalanceDao balanceDao;
    private final ErrorHandler handler;
    private OutputStream os;

    Controller(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ErrorHandler handler) {
        this.userDao = userDao;
        this.balanceDao = balanceDao;
        this.handler = handler;
    }

    String throwException(Throwable throwable) {
        return handler.getException(throwable);
    }

    String sendOK(String data) {
        ResponseData os = new ResponseData();
        os.setCode(HTTP_OK);
        os.setContentLength(String.valueOf(data.length()));
        os.setMessage(data);
        return os.getBody();
    }
}
