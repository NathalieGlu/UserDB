package ru.nathalie.model.dao;

import ru.nathalie.db.Connector;

public class RegistrationDaoImpl implements RegistrationDao {
    private final Connector connector;

    public RegistrationDaoImpl(Connector connector) {
        this.connector = connector;
    }

    @Override
    public boolean createUser(String name, String phone, String mail) {
        return connector.createUser(name, phone, mail);
    }


}
