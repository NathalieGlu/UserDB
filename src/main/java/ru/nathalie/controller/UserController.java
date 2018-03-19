package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.UserDao;
import ru.nathalie.model.dao.UserDaoImpl;

public class UserController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class.getName());
    private final UserDao userDao;

    public UserController(ErrorHandler handler, UserDaoImpl userDao) {
        super(handler);
        this.userDao = userDao;
    }

    public String getUsers(String is) {
        String jsonAnswer = userDao.getUsers();
        log.debug("Got users from database");
        return sendOK(jsonAnswer);
    }
}
