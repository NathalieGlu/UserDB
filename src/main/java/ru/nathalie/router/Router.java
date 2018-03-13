package ru.nathalie.router;

import ru.nathalie.controller.Controller;
import ru.nathalie.model.iostream.InputStreamData;
import ru.nathalie.model.iostream.OutputStreamData;

import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

public class Router {
    private static final String HTTP_BAD_REQUEST = "400 Bad Request\n";
    private final Controller controller;
    private Map<String, BiConsumer<InputStreamData, OutputStreamData>> mappings = new HashMap<>();

    public Router(Controller controller) {
        this.controller = controller;
        setRouter();
    }

    public void parseHeaders(InputStreamData isData, Socket socket) throws IOException {
        controller.setSocket(socket);

        OutputStreamData osData = new OutputStreamData();
        if (mappings.containsKey(isData.getMapping())) {
            mappings.get(isData.getMapping()).accept(isData, osData);
        } else {
            controller.throwException(new IOException(HTTP_BAD_REQUEST));
        }
    }

    public void setRouter() {
        mappings.put("/users", controller::getUsers);
        mappings.put("/balance", controller::getBalance);
    }
}
