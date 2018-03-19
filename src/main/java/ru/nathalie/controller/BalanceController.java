package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.BalanceDao;
import ru.nathalie.model.dao.BalanceDaoImpl;

import java.io.IOException;

public class BalanceController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(BalanceController.class.getName());
    final BalanceDao balanceDao;

    public BalanceController(BalanceDaoImpl balanceDao, ErrorHandler handler) {
        super(handler);
        this.balanceDao = balanceDao;
    }

    public String getBalance(String args) {
        try {
            Integer id = getId(args);
            String jsonAnswer = balanceDao.getBalance(id);

            if (jsonAnswer != null) {
                return sendOK(jsonAnswer);
            } else {
                log.error("user_id not found!");
                return throwException(new IOException(HTTP_NOT_FOUND));
            }
        } catch (NumberFormatException e) {
            log.error("Bad request!");
            return throwException(new IOException(HTTP_BAD_REQUEST));
        }
    }

    private int getId(String mapping) {
        return Integer.parseInt(mapping.substring(mapping.indexOf("=") + 1, mapping.length()));
    }
}
