package ru.nathalie.controller;

import ru.nathalie.config.AppProperties;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dao.RegistrationDao;
import ru.nathalie.model.dao.RegistrationDaoImpl;

import java.io.IOException;

public class RegistrationController extends Controller {
    private final static String PHONE = "phone=";
    private final static String NAME = "name=";
    private final static String EMAIL = "email=";

    private final RegistrationDao registrationDao;
    private final AppProperties properties;
    private String name;
    private String phoneNumber;
    private String email;

    public RegistrationController(ErrorHandler handler, RegistrationDaoImpl registrationDao, AppProperties properties) {
        super(handler);
        this.registrationDao = registrationDao;
        this.properties = properties;
    }

    public String registerNewUser(String userData) {
        if (getName(userData) && getPhoneNumber(userData) && getMail(userData)) {
            if (registrationDao.createUser(name, phoneNumber, email)) {
                return sendOK("User was registered");
            } else {
                return throwException(new IOException(HTTP_CONFLICT));
            }
        } else {
            return throwException(new IOException(HTTP_BAD_REQUEST));
        }

    }

    private boolean getName(String userData) {
        name = userData.substring(userData.indexOf(NAME) + NAME.length(), userData.indexOf("?phone"));
        return true;
    }

    private boolean getPhoneNumber(String userData) {
        String phoneNumber = userData.substring(userData.indexOf(PHONE) + PHONE.length(), userData.indexOf("?email"));
        if (phoneNumber.length() != properties.getPhoneLength()) {
            return false;
        } else {
            this.phoneNumber = phoneNumber;
            return true;
        }
    }

    private boolean getMail(String userData) {
        String email = userData.substring(userData.indexOf(EMAIL) + EMAIL.length(), userData.length());
        if (properties.getDomains().contains(email.substring(email.indexOf("@"), email.length()))) {
            this.email = email;
            return true;
        } else {
            return false;
        }
    }
}
