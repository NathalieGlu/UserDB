package ru.nathalie.dao;

import org.json.JSONObject;
import ru.nathalie.db.Connector;

public class UserDaoImpl implements UserDao {

    private Connector connector;

    public UserDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public JSONObject getUsers() {
        return connector.getUsers();
    }
}
