package ru.nathalie.factory;

import ru.nathalie.config.AppProperties;
import ru.nathalie.dao.BalanceDaoImpl;
import ru.nathalie.dao.UserDaoImpl;
import ru.nathalie.db.ConnectionPool;
import ru.nathalie.db.Connector;
import ru.nathalie.web.WebServer;

public class ServerFactory {
    public static WebServer build() {
        Connector connector = new Connector(new ConnectionPool(new AppProperties()).setPool());
        return new WebServer(new UserDaoImpl(connector), new BalanceDaoImpl(connector));
    }
}
