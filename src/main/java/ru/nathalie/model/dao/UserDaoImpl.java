package ru.nathalie.model.dao;

import ru.nathalie.db.Connector;

public class UserDaoImpl implements UserDao {
    private final Connector connector;

    public UserDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public String getUsers() {
        return connector.getUsers().toString();
    }
}
