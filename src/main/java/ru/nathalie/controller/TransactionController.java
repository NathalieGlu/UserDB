package ru.nathalie.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.TransactionDao;
import ru.nathalie.model.dao.TransactionDaoImpl;

import java.io.IOException;

public class TransactionController extends Controller {
    private static final Logger log = LoggerFactory.getLogger(TransactionController.class.getName());
    private static final String FROM = "from=";
    private static final String TO = "to=";
    private static final String AMOUNT = "amount=";
    private final TransactionDao transactionDao;

    public TransactionController(ErrorHandler handler, TransactionDaoImpl transactionDao) {
        super(handler);
        this.transactionDao = transactionDao;
    }

    public String makeTransaction(String args) {
        try {
            Integer fromId = getFromId(args);
            Integer toId = getToId(args);
            Double amount = getAmount(args);

            String response = transactionDao.makeTransaction(fromId, toId, amount);

            if (response.equals("OK")) {
                return sendOK("Transaction complete");
            } else {
                log.error(response);
                return throwException(new IOException(HTTP_BAD_REQUEST), response);
            }
        } catch (Exception e) {
            log.error("Bad request!");
            return throwException(new IOException(HTTP_BAD_REQUEST));
        }
    }

    private Integer getFromId(String args) {
        return Integer.parseInt(args.substring(args.indexOf(FROM) + FROM.length(), args.indexOf(TO) - 1));
    }

    private Integer getToId(String args) {
        return Integer.parseInt(args.substring(args.indexOf(TO) + TO.length(), args.indexOf(AMOUNT) - 1));
    }

    private Double getAmount(String args) {
        return Double.valueOf(args.substring(args.indexOf(AMOUNT) + AMOUNT.length()));
    }
}
