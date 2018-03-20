package ru.nathalie.router;

import ru.nathalie.controller.BalanceController;
import ru.nathalie.controller.RegistrationController;
import ru.nathalie.controller.TransactionController;
import ru.nathalie.controller.UserController;
import ru.nathalie.handler.ErrorHandler;
import ru.nathalie.model.dto.RequestDto;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Router {
    private static final String HTTP_NOT_FOUND = "404 Not Found\n";
    private final UserController userController;
    private final BalanceController balanceController;
    private final RegistrationController registrationController;
    private final TransactionController transactionController;
    private final ErrorHandler handler;
    private final Map<String, Function<String, String>> mappings = new HashMap<>();

    public Router(UserController userController, BalanceController balanceController,
                  RegistrationController registrationController, TransactionController transactionController,
                  ErrorHandler handler) {
        this.userController = userController;
        this.balanceController = balanceController;
        this.registrationController = registrationController;
        this.transactionController = transactionController;
        this.handler = handler;
        setRouter();
    }

    public String parseHeaders(RequestDto data) {
        if (mappings.containsKey(data.getMapping())) {
            return mappings.get(data.getMapping()).apply(data.getArgs());
        } else {
            return handler.getException(new IOException(HTTP_NOT_FOUND));
        }
    }

    private void setRouter() {
        mappings.put("/users", userController::getUsers);
        mappings.put("/balance", balanceController::getBalance);
        mappings.put("/register", registrationController::registerNewUser);
        mappings.put("/transaction", transactionController::makeTransaction);
    }
}
