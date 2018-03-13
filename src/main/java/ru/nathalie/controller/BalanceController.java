package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ExcHandler;
import ru.nathalie.model.dao.BalanceDaoImpl;
import ru.nathalie.model.dao.UserDaoImpl;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.model.iostream.OutputStreamData;

import java.io.IOException;

public class BalanceController extends Controller{
    private static final Logger log = LoggerFactory.getLogger(BalanceController.class.getName());

    public BalanceController(UserDaoImpl userDao, BalanceDaoImpl balanceDao, ExcHandler handler) {
        super(userDao, balanceDao, handler);
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

    private int getId(String mapping) {
        return Integer.parseInt(mapping.substring(mapping.indexOf("=") + 1, mapping.length()));
    }
}
