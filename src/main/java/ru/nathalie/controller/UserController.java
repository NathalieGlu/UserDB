package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDaoImpl;

public class UserController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class.getName());

    public UserController(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ErrorHandler handler) {
        super(userDao, balanceDao, handler);
    }

    public String getUsers(String is) {
        String jsonAnswer = userDao.getUsers();
        log.info("Got users from database");
        return sendOK(jsonAnswer);
    }
}
