package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ExcHandler;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDaoImpl;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.model.iostream.OutputStreamData;

public class UserController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(UserController.class.getName());

    public UserController(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ExcHandler handler) {
        super(userDao, balanceDao, handler);
    }

    public void getUsers(InputStreamData is, OutputStreamData os) {
        String jsonAnswer = userDao.getUsers();
        os.setCode(HTTP_OK);
        os.setContentLength(String.valueOf(jsonAnswer.length()));
        os.setMessage(jsonAnswer);

        log.info("Got users from database");
        writeResponse(os.getBody());
    }
}
