package ru.nathalie.router;

import ru.nathalie.controller.BalanceController;
import ru.nathalie.controller.UserController;
import ru.nathalie.handler.ExcHandler;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.model.iostream.OutputStreamData;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Router {
    private static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    private Map<String, BiConsumer<InputStreamData, OutputStreamData>> mappings = new HashMap<>();
    private final UserController userController;
    private final BalanceController balanceController;
    private final ExcHandler handler;

    public Router(UserController userController, BalanceController balanceController, ExcHandler handler) {
        this.userController = userController;
        this.balanceController = balanceController;
        this.handler = handler;
        setRouter();
    }

    public void parseHeaders(InputStreamData isData, Socket socket) throws IOException {
        userController.setSocket(socket);
        balanceController.setSocket(socket);

        OutputStreamData osData = new OutputStreamData();
        if (mappings.containsKey(isData.getMapping())) {
            mappings.get(isData.getMapping()).accept(isData, osData);
        } else {
            handler.printException(new IOException(HTTP_BAD_REQUEST));
        }
    }

    public void setRouter() {
        mappings.put("/users", userController::getUsers);
        mappings.put("/balance", balanceController::getBalance);
    }
}
