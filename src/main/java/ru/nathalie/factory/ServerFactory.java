package ru.nathalie.factory;

import ru.nathalie.config.AppProperties;
import ru.nathalie.dao.UserDaoImpl;
import ru.nathalie.db.ConnectionPool;
import ru.nathalie.db.Connector;
import ru.nathalie.web.WebServer;

public class ServerFactory {
    public static WebServer build() {
        return new WebServer(new UserDaoImpl(new Connector(new ConnectionPool(new AppProperties()).setPool())));
    }
}
